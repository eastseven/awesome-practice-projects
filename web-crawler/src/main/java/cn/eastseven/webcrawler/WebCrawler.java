package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.pipeline.ChinaPubPipeline;
import cn.eastseven.webcrawler.pipeline.WinXuanPipeline;
import cn.eastseven.webcrawler.service.ProxyService;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class WebCrawler implements CommandLineRunner {

    final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";

    @Autowired
    ProxyService proxyService;

    @Autowired
    ChinaPubPipeline chinaPubPipeline;

    @Autowired
    WinXuanPipeline winXuanPipeline;

    @Autowired
    RedisScheduler redisScheduler;

    public void start() {
        log.debug("===== start =====");

        int size = Runtime.getRuntime().availableProcessors() * 2;

        Spider chinaPubSpider = OOSpider.create(SiteUtil.get(), chinaPubPipeline, ChinaPub.class).setScheduler(redisScheduler)
                .addUrl("http://www.china-pub.com/?" + System.currentTimeMillis())
                .thread(size);


        Spider winXuanSpider = OOSpider.create(SiteUtil.get().setUserAgent(USER_AGENT), winXuanPipeline, WinXuan.class).setScheduler(redisScheduler)
                .addUrl("http://www.winxuan.com/?" + System.currentTimeMillis())
                .thread(size);

        winXuanSpider.start();
        chinaPubSpider.start();

        log.debug("===== end =====");
    }

    @Scheduled(cron = "0 0/50 * * * ?")
    public void task() {
        start();
    }

    @Override
    public void run(String... strings) throws Exception {
        start();
    }
}
