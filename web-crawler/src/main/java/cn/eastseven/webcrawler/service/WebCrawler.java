package cn.eastseven.webcrawler.service;

import cn.eastseven.webcrawler.model.*;
import cn.eastseven.webcrawler.pipeline.GongGongZiYuanPipeline;
import cn.eastseven.webcrawler.pipeline.MongoPipeline;
import cn.eastseven.webcrawler.processor.GongGongZiYuanPageProcessor;
import cn.eastseven.webcrawler.repository.BookCategoryRepository;
import cn.eastseven.webcrawler.repository.ChinaPubRepository;
import cn.eastseven.webcrawler.repository.DangDangRepository;
import cn.eastseven.webcrawler.repository.WinXuanRepository;
import cn.eastseven.webcrawler.utils.GongGongZiYuanUtil;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.RedisScheduler;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Service
@Order(value = 2)
public class WebCrawler implements CommandLineRunner, DisposableBean {

    public static final String REQUEST_IGNORE = "ignore";

    @Autowired
    ApplicationContext context;

    @Autowired
    ProxyService proxyService;

    @Autowired
    RedisScheduler redisScheduler;

    @Autowired
    MongoPipeline mongoPipeline;

    @Autowired
    ChinaPubRepository chinaPubRepository;

    @Autowired
    DangDangRepository dangDangRepository;

    @Autowired
    WinXuanRepository winXuanRepository;

    @Autowired
    BookCategoryRepository categoryRepository;

    ExecutorService spiderExecutorService;

    final Class[] pageModels = {
            ChinaPub.class,
            WinXuan.class,
            DangDang.class
    };

    final BookOrigin[] origins = {
            BookOrigin.CHINA_PUB,
            BookOrigin.WIN_XUAN,
            BookOrigin.DANG_DANG
    };

    private List<Spider> spiderList = Lists.newArrayList();

    public List<Spider> getSpiderList() {
        return spiderList;
    }

    private ExecutorService initExecutorService() {
        final int size = Runtime.getRuntime().availableProcessors();
        final int max = size * size;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(size);
        executor.setMaxPoolSize(max);
        executor.setThreadNamePrefix("crawler-");
        executor.initialize();
        log.info("ThreadPoolTaskExecutor param >>> core {}, max {}", size, max);
        spiderExecutorService = executor.getThreadPoolExecutor();

        return spiderExecutorService;
    }

    @PostConstruct
    public void init() {
        initExecutorService();

        for (Class pageModel : pageModels) {
            SeedUrl seedUrl = AnnotationUtils.findAnnotation(pageModel, SeedUrl.class);
            log.debug("seedUrl value= {}", Arrays.toString(seedUrl.value()));

            Request[] requests = new Request[seedUrl.value().length];
            for (int index = 0; index < requests.length; index++) {
                Request request = new Request(seedUrl.value()[index]);
                request.putExtra("ignore", Boolean.TRUE);
                requests[index] = request;
            }
            Spider spider = getSpider(pageModel);
            spider.addRequest(requests);
            spider.setUUID("book-spider-" + pageModel.getSimpleName().toLowerCase());
            spiderList.add(spider);
        }

        for (BookOrigin origin : origins) {
            List<BookCategory> bookCategoryList = categoryRepository.findByOrigin(origin);
            if (CollectionUtils.isEmpty(bookCategoryList)) continue;

            Class pageModel = null;
            switch (origin) {
                case CHINA_PUB:
                    pageModel = ChinaPub.class;
                    break;
                case WIN_XUAN:
                    pageModel = WinXuan.class;
                    break;
                case DANG_DANG:
                    pageModel = DangDang.class;
                    break;
                default:
                    break;
            }

            Spider spider = getSpider(pageModel);
            spider.setUUID("book-category-spider-" + pageModel.getSimpleName().toLowerCase());

            List<Request> requests = bookCategoryList.stream().map(category -> {
                Request request = new Request(category.getUrl());
                request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
                return request;
            }).collect(Collectors.toList());

            spider.addRequest(requests.get(RandomUtils.nextInt(0, requests.size() - 1)));

            spiderList.add(spider);
        }
    }

    private Spider getSpider(Class pageModel) {
        return OOSpider.create(SiteUtil.get(), mongoPipeline, pageModel)
                .setScheduler(redisScheduler)
                .setExitWhenComplete(false)
                .thread(Runtime.getRuntime().availableProcessors() * 4)
                .setExecutorService(spiderExecutorService);
    }

    public void start() {
        log.info(">>> start <<<");
        for (Spider spider : spiderList) {
            spider.start();
        }
        log.info(">>> end <<<");
    }

    public void update() {
        update(BookOrigin.WIN_XUAN);
        update(BookOrigin.CHINA_PUB);
        update(BookOrigin.DANG_DANG);
    }

    public void update(BookOrigin origin) {
        final int size = 10;
        int totalPages;
        Spider spider = null;
        switch (origin) {
            case CHINA_PUB:
                spider = OOSpider.create(SiteUtil.get(), mongoPipeline, ChinaPub.class)
                        .setScheduler(redisScheduler).thread(spiderExecutorService, 1);

                Page<ChinaPub> first = chinaPubRepository.findByCreateTimeIsNull(new PageRequest(0, size));
                if (!first.hasContent()) {
                    break;
                }

                totalPages = first.getTotalPages();
                log.info("\t>>>\tCHINA_PUB\ttotal={}, total pages={}", first.getTotalElements(), totalPages);

                for (int page = 0; page < totalPages; page++) {
                    PageRequest pageRequest = new PageRequest(page, size);
                    Page<ChinaPub> chinaPubPage = chinaPubRepository.findByCreateTimeIsNull(pageRequest);
                    for (ChinaPub chinaPub : chinaPubPage) {
                        Request request = new Request(chinaPub.getUrl());
                        request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
                        spider.addRequest(request);
                    }

                    if (page > 1) {
                        break;
                    }
                }

                break;
            case DANG_DANG:
                spider = OOSpider.create(SiteUtil.get(), mongoPipeline, DangDang.class)
                        .setScheduler(redisScheduler).thread(spiderExecutorService, 1);

                Page<DangDang> firstDangDangPage = dangDangRepository.findByCreateTimeIsNull(new PageRequest(0, size));
                if (!firstDangDangPage.hasContent()) {
                    break;
                }

                totalPages = firstDangDangPage.getTotalPages();
                log.info("\t>>>\tDANG_DANG\ttotal={}, total pages={}", firstDangDangPage.getTotalElements(), totalPages);

                for (int page = 0; page < totalPages; page++) {
                    PageRequest pageRequest = new PageRequest(page, size);
                    Page<DangDang> dangDangPage = dangDangRepository.findByCreateTimeIsNull(pageRequest);
                    for (DangDang dangDang : dangDangPage) {
                        Request request = new Request(dangDang.getUrl());
                        request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
                        spider.addRequest(request);
                    }

                    if (page > 1) {
                        break;
                    }
                }

                break;
            case WIN_XUAN:
                spider = OOSpider.create(SiteUtil.get(), mongoPipeline, WinXuan.class)
                        .setScheduler(redisScheduler).thread(spiderExecutorService, 1);

                Page<WinXuan> firstWinXuanPage = winXuanRepository.findByCreateTimeIsNull(new PageRequest(0, size));
                if (!firstWinXuanPage.hasContent()) {
                    break;
                }

                totalPages = firstWinXuanPage.getTotalPages();
                log.info("\t>>>\tWIN_XUAN\ttotal={}, total pages={}", firstWinXuanPage.getTotalElements(), totalPages);

                for (int page = 0; page < totalPages; page++) {
                    PageRequest pageRequest = new PageRequest(page, size);
                    Page<WinXuan> winXuanPage = winXuanRepository.findByCreateTimeIsNull(pageRequest);
                    for (WinXuan winXuan : winXuanPage) {
                        Request request = new Request(winXuan.getUrl());
                        request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
                        spider.addRequest(request);
                    }

                    if (page > 1) {
                        break;
                    }
                }
                break;

            default:
                break;
        }

        if (spider == null) {
            return;
        }
        log.info("{} page count {}", origin, spider.getPageCount());
        spider.setUUID("book-spider-update-" + origin.name().toLowerCase());
        spiderList.add(spider);
        spider.start();
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info(" === Entry {}", Arrays.toString(strings));
        for (String param : strings) {
            switch (param) {
                case "start":
                    start();
                    break;
                case "updateChinaPub":
                    update(BookOrigin.CHINA_PUB);
                    break;
                case "update":
                    update();
                    break;
                case "ggzy":
                    ggzy();
                    break;
                default:
                    break;
            }
        }
    }

    public void ggzy() {
        Spider spider = Spider.create(context.getBean(GongGongZiYuanPageProcessor.class));
        Request[] requests = new Request[365];
        for (int day = 0; day < requests.length; day++) {
            String date = DateTime.now().minusDays(day).toString("yyyy-MM-dd");
            Request request = new Request("http://deal.ggzy.gov.cn/ds/deal/dealList.jsp");
            request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
            request.putExtra("date", date);
            request.setMethod(HttpConstant.Method.POST);
            request.setRequestBody(HttpRequestBody.form(GongGongZiYuanUtil.getPostPageParams(date, date), "UTF-8"));
            log.debug("{}", GongGongZiYuanUtil.getPostPageParams(date, date).get("TIMEBEGIN"));

            requests[day] = request;
        }
        spider.addRequest(requests);
        spider.thread(spiderExecutorService, 32);
        /*String date = "2017-10-01";//DateTime.now().minusDays(day).toString("yyyy-MM-dd");
        Request request = new Request("http://deal.ggzy.gov.cn/ds/deal/dealList.jsp");
        request.putExtra(REQUEST_IGNORE, Boolean.TRUE);
        request.putExtra("date", date);
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.form(GongGongZiYuanUtil.getPostPageParams(date, date), "UTF-8"));
        spider.addRequest(request);*/

        //spider.setScheduler(redisScheduler);
        //spider.setExecutorService(executorService);
        spider.addPipeline(context.getBean(GongGongZiYuanPipeline.class));

        spider.run();
    }

    @Override
    public void destroy() throws Exception {
        for (Spider spider : spiderList) {
            if (spider.getStatus().equals(Spider.Status.Running)) {
                spider.stop();
            }

            Thread.sleep(1234L);

            if (spider.getStatus().equals(Spider.Status.Stopped)) {
                spider.close();
            }
        }
    }
}
