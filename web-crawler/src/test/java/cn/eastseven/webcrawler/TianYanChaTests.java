package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.downloader.WebDriverDownloader;
import cn.eastseven.webcrawler.model.JD;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TianYanChaTests {

    final String driver = "/Users/dongqi/Dev/code/work-har/har-sjfxpt/har-business-crawler-tianyancha/libs/selenium/mac/chromedriver";

    @Autowired
    WebDriverDownloader downloader;

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", driver);
    }

    @Test
    public void test() {

        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                //log.debug("\n{}", page.getHtml().getDocument().body());
            }

            @Override
            public Site getSite() {
                return Site.me();
            }
        }).setDownloader(new WebDriverDownloader());
        Assert.assertNotNull(spider);
        //spider.test("https://www.jd.com");

        spider.addUrl("https://item.jd.com/4595061.html").run();
    }

    @Test
    public void testJD() {
        String[] urls = {
                "https://shouji.jd.com/", "https://diannao.jd.com/",
                "https://jiadian.jd.com/",
                "http://channel.jd.com/men.html",
                "https://channel.jd.com/home.html",
                "https://book.jd.com/",
        };

        for (String url : urls) {
            Spider spider = OOSpider.create(SiteUtil.get(), new PageModelPipeline() {
                @Override
                public void process(Object o, Task task) {
                    System.out.println(o);
                }
            }, JD.class);
            Assert.assertNotNull(spider);
            spider.setDownloader(downloader).addUrl(url);
            spider.start();
            try {
                Thread.sleep(1234L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
