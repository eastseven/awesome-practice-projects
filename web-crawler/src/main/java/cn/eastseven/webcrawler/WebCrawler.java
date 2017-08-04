package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.pipeline.MySQLPipeline;
import cn.eastseven.webcrawler.processor.ChinaPubProcessor;
import cn.eastseven.webcrawler.processor.WinXuanProcessor;
import cn.eastseven.webcrawler.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class WebCrawler {

    @Autowired
    ChinaPubProcessor chinaPubProcessor;

    @Autowired
    WinXuanProcessor winXuanProcessor;

    @Autowired
    MySQLPipeline mySQLPipeline;

    @Autowired
    CategoryRepository categoryRepository;

    public void start() {
        log.debug("===== start =====");

        categoryRepository.findAll().forEach(category -> {
            String url = category.getUrl();
            if (url.contains("china-pub")) {
                Spider.create(chinaPubProcessor).addPipeline(mySQLPipeline).addUrl(url).run();
            } else if (url.contains("winxuan")) {
                Spider.create(winXuanProcessor).addPipeline(mySQLPipeline).addUrl(url).run();
            }
        });

        log.debug("===== end =====");
    }

}
