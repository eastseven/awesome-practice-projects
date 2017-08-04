package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.pipeline.MySQLPipeline;
import cn.eastseven.webcrawler.processor.CategoryProcessor;
import cn.eastseven.webcrawler.processor.ChinaPubProcessor;
import cn.eastseven.webcrawler.processor.WinXuanProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import us.codecraft.webmagic.Spider;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class WebCrawlerApplication implements CommandLineRunner {

    @Autowired
    CategoryProcessor processor;

    @Autowired
    ChinaPubProcessor chinaPubProcessor;

    @Autowired
    WinXuanProcessor winXuanProcessor;

    @Autowired
    MySQLPipeline pipeline;

    @Autowired
    WebCrawler webCrawler;

    public static void main(String[] args) {
        SpringApplication.run(WebCrawlerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        Spider.create(processor)
                //从"https://github.com/code4craft"开始抓
                .addUrl("http://www.china-pub.com/browse/")
                .addUrl("http://www.winxuan.com/catalog_book.html")
                .addPipeline(pipeline)
                //开启5个线程抓取
                .thread(Runtime.getRuntime().availableProcessors())
                //启动爬虫
                .run();
        /*
		Spider.create(winXuanProcessor)
				//.addUrl("http://product.china-pub.com/cache/browse2/59/1_1_59_0.html")
				.addUrl("http://list.winxuan.com/1082")
				.addPipeline(pipeline)
				.run();*/

        webCrawler.start();
    }

}
