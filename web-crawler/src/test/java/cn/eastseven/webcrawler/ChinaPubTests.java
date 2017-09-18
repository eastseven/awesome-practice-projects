package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.ChinaPub;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

@Slf4j
public class ChinaPubTests extends WebCrawlerApplicationTests {

    @Test
    public void test() {
        Spider spider = OOSpider.create(Site.me(), ChinaPub.class).addUrl("http://product.china-pub.com/6225318");
        spider.run();
        Assert.assertNotNull(spider);
    }
}
