package cn.eastseven.web.crawler;

import cn.eastseven.web.crawler.processor.QiXinPageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

@Slf4j
public class QiXinPageProcessorTests extends WebCrawlerWithHdfsApplicationTests {

    @Autowired
    QiXinPageProcessor pageProcessor;

    @Test
    public void test() {
        Spider.create(pageProcessor)
                //.addUrl("http://www.qixin.com/company/c7fedcf3-50bb-45c2-a9a5-a5dc0c6e9c36")
                //.addUrl("https://www.tianyancha.com/search?key=%E6%B1%87%E5%AE%89%E8%9E%8D")
                //.addUrl("https://www.tianyancha.com/company/651442")
                //.addUrl("https://baike.baidu.com/item/%E5%9B%9B%E5%B7%9D%E6%B1%87%E5%AE%89%E8%9E%8D%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%8D%E5%8A%A1%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8")
                .run();
        Assert.assertNotNull(pageProcessor);
    }
}
