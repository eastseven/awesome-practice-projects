package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.BookCategory;
import cn.eastseven.webcrawler.model.BookOrigin;
import cn.eastseven.webcrawler.repository.BookCategoryRepository;
import cn.eastseven.webcrawler.service.WebCrawler;
import cn.eastseven.webcrawler.service.WebCrawlerStatistics;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class WebCrawlerTests extends WebCrawlerApplicationTests {

    @Autowired
    WebCrawler webCrawler;

    @Autowired
    WebCrawlerStatistics webCrawlerStatistics;

    @Autowired
    BookCategoryRepository categoryRepository;

    @Test
    public void test() {
        Assert.assertNotNull(webCrawler);
        Assert.assertFalse(webCrawler.getSpiderList().isEmpty());
        webCrawler.getSpiderList().forEach(spider -> spider.run());
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

    @Test
    public void testCategory() {
        BookOrigin[] origins = {
                BookOrigin.CHINA_PUB, BookOrigin.WIN_XUAN, BookOrigin.DANG_DANG
        };

        for (BookOrigin origin : origins) {
            List<BookCategory> list = categoryRepository.findByOrigin(origin);
            Assert.assertNotNull(list);
            log.debug(">>> {} category size {}", origin, list.size());
        }
    }
}
