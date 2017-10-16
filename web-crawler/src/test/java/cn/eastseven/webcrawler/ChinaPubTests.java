package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.ChinaPub;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.scheduler.Scheduler;

@Slf4j
public class ChinaPubTests extends WebCrawlerApplicationTests {

    @Test
    public void test() {
        Spider spider = OOSpider.create(Site.me(), ChinaPub.class).addUrl("http://product.china-pub.com/6225318");
        spider.run();
        Assert.assertNotNull(spider);
    }

    @Test
    public void testScheduler() {
        String url = "http://product.china-pub.com/6225318";
        OOSpider.create(Site.me(), ChinaPub.class)
                .addUrl(url)
                .setScheduler(new Scheduler() {
                    @Override
                    public void push(Request request, Task task) {
                        log.debug(" push >>> {}, {}, {}, {}", request.getUrl(), task.getSite(), task.getUUID(), task.getClass());
                    }

                    @Override
                    public Request poll(Task task) {
                        log.debug(" poll >>> {}, {}, {}", task.getSite(), task.getUUID(), task.getClass());
                        return null;
                    }
                })
                .run();

        Assert.assertNotNull(url);
    }
}
