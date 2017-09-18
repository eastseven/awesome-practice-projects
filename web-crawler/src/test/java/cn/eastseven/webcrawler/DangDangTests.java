package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.DangDang;
import cn.eastseven.webcrawler.pipeline.MongoPipeline;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;

@Slf4j
public class DangDangTests extends WebCrawlerApplicationTests {

    @Autowired
    MongoPipeline mongoPipeline;

    @Test
    public void test() {
        String url = "http://book.dangdang.com/";
        Assert.assertNotNull(url);

        OOSpider.create(SiteUtil.get(), new ConsolePageModelPipeline(), DangDang.class)
                .addUrl(url).run();
    }

    @Test
    public void testDangDang() {
        String url = "http://book.dangdang.com/";
        url = "http://product.dangdang.com/25124461.html";
        Assert.assertNotNull(url);

        OOSpider.create(SiteUtil.get(), mongoPipeline, DangDang.class)
                .addUrl(url).run();
    }
}
