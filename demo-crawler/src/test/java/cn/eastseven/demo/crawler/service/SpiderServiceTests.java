package cn.eastseven.demo.crawler.service;

import cn.eastseven.demo.crawler.DemoCrawlerApplicationTests;
import cn.eastseven.demo.crawler.model.SpiderConfig;
import cn.eastseven.demo.crawler.repository.SpiderConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

/**
 * @author eastseven
 */
@Slf4j
public class SpiderServiceTests extends DemoCrawlerApplicationTests {

    @Autowired private SpiderService spiderService;

    @Autowired private SpiderConfigRepository dao;

    @Before
    public void init() {
        long size = dao.count();
        if (size == 0L) {
            SpiderConfig config = SpiderConfig.builder()
                    .url("http://news.163.com/")
                    .targetElement("#endText")
                    .regExp("http://news.163.com/\\d+/\\d+/\\d+/\\w+.html")
                    .keywords("习近平 市民")
                    .build();
            dao.save(config);
        }
    }

    @Test
    public void testStart() {
        spiderService.start();
    }

    @Test
    public void testRegExp() {
        String url1 = "http://news.163.com/18/0409/08/DEUHECSQ0001875N.html";
        String url2 = "http://news.163.com";

        Pattern pattern = Pattern.compile("http://news.163.com/\\d+/\\d+/\\d+/\\w+.html");
        Assert.assertTrue(pattern.matcher(url1).find());
        Assert.assertFalse(pattern.matcher(url2).find());
    }
}
