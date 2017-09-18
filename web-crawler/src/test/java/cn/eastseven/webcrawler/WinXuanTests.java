package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.service.ProxyService;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.OOSpider;

@Slf4j
public class WinXuanTests extends WebCrawlerApplicationTests {

    @Autowired
    ProxyService proxyService;

    @Test
    public void test() {
        Spider spider = OOSpider.create(SiteUtil.get(), WinXuan.class);
        spider.setDownloader(proxyService.getDownloader());
        spider.test("http://item.winxuan.com/1201557652");
        Assert.assertNotNull(spider);
    }
}
