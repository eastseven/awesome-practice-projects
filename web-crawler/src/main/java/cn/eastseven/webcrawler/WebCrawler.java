package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.pipeline.ChinaPubPipeline;
import cn.eastseven.webcrawler.pipeline.WinXuanPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class WebCrawler {

    final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";

    @Autowired
    ChinaPubPipeline chinaPubPipeline;

    @Autowired
    WinXuanPipeline winXuanPipeline;

    public void start() {
        log.debug("===== start =====");


        log.debug("===== end =====");
    }

    @Async
    public void startChinaPub() {
        start(chinaPubPipeline, ChinaPub.class, "http://www.china-pub.com/");
    }

    @Async
    public void startWinXuan() {
        start(winXuanPipeline, WinXuan.class, "http://www.winxuan.com/");
    }

    public void start(PageModelPipeline pageModelPipeline, Class clazz, String startUrl) {
        Site site = Site.me().setSleepTime(1000).setTimeOut(100000).setCycleRetryTimes(3).setUserAgent(USER_AGENT);
        OOSpider.create(site, pageModelPipeline, clazz)
                .addUrl(startUrl)
                .setScheduler(new FileCacheQueueScheduler("data"))
                .run();
    }
}
