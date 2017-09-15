package cn.eastseven.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class WebCrawlerTests extends WebCrawlerApplicationTests {

    @Autowired
    WebCrawler webCrawler;

    @Test
    public void test() {
        Assert.assertNotNull(webCrawler);
        webCrawler.start();
    }

}
