package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.BookOrigin;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WebCrawlerTests extends WebCrawlerApplicationTests {

    @Autowired
    WebCrawler webCrawler;

    @Autowired
    WebCrawlerStatistics webCrawlerStatistics;

    @Test
    public void test() {
        Assert.assertNotNull(webCrawler);
        webCrawler.start();
    }

    @Test
    public void testUpdate() {
        Assert.assertNotNull(webCrawler);
        webCrawler.update(BookOrigin.WIN_XUAN);
        //webCrawler.update(BookOrigin.DANG_DANG);
        //webCrawler.update(BookOrigin.CHINA_PUB);
    }

    @Test
    public void testCount() {
        Assert.assertNotNull(webCrawlerStatistics);
        webCrawlerStatistics.count();
    }

    @Test
    public void testGgzy() {
        Assert.assertNotNull(webCrawler);
        webCrawler.ggzy();
    }
}
