package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.downloader.WebDriverDownloader;
import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.model.DangDang;
import cn.eastseven.webcrawler.model.SeedUrl;
import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.pipeline.ChinaPubPipeline;
import cn.eastseven.webcrawler.pipeline.MongoPipeline;
import cn.eastseven.webcrawler.pipeline.WinXuanPipeline;
import cn.eastseven.webcrawler.service.ProxyService;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Transactional
@Order(value = 2)
public class WebCrawler implements CommandLineRunner {

    @Autowired
    ApplicationContext context;

    @Autowired
    ProxyService proxyService;

    @Autowired
    ChinaPubPipeline chinaPubPipeline;

    @Autowired
    WinXuanPipeline winXuanPipeline;

    @Autowired
    RedisScheduler redisScheduler;

    @Autowired
    MongoPipeline mongoPipeline;

    @Autowired
    WebDriverDownloader downloader;

    final Class[] pageModels = {
            ChinaPub.class,
            WinXuan.class,
            DangDang.class
    };

    public void start() {
        log.info(">>> start <<<");

        int size = Runtime.getRuntime().availableProcessors() * 2;

        ExecutorService executorService = Executors.newScheduledThreadPool(size);

        List<Spider> spiderList = Lists.newArrayList();
        for (Class pageModel : pageModels) {
            SeedUrl seedUrl = AnnotationUtils.findAnnotation(pageModel, SeedUrl.class);
            log.debug("seedUrl value= {}", Arrays.toString(seedUrl.value()));
            String[] urls = new String[seedUrl.value().length];

            //TODO 暂时在URL后面加时间，避免被redisScheduler判定为已经抓取过的
            for (int index = 0; index < urls.length; index++) {
                urls[index] = seedUrl.value()[index] + "?" + System.currentTimeMillis();
            }

            Spider spider = OOSpider.create(SiteUtil.get(), mongoPipeline, pageModel)
                    .setScheduler(redisScheduler)
                    .addUrl(urls)
                    .thread(executorService, size);

            /*
            if (pageModel.equals(WinXuan.class)) {
                spider.setDownloader(downloader);
            }*/

            spiderList.add(spider);
        }

        for (Spider spider : spiderList) {
            spider.start();
        }

        log.info(">>> end <<<");
    }

    //@Scheduled(cron = "0 0/50 * * * ?")
    public void task() {
        start();
    }

    public void winxuan() {
        final int size = Runtime.getRuntime().availableProcessors() * 2;

        OOSpider.create(SiteUtil.get(), mongoPipeline, WinXuan.class)
                //.setDownloader(downloader)
                .addUrl("http://www.winxuan.com/")
                .thread(Executors.newFixedThreadPool(size), size)
                .run();
    }

    @Override
    public void run(String... strings) throws Exception {
        log.info(" === Entry {}", Arrays.toString(strings));
        for (String param : strings) {
            switch (param) {
                case "start":
                    start();
                    break;
                case "winxuan":
                    winxuan();
                    break;
            }
        }
    }
}
