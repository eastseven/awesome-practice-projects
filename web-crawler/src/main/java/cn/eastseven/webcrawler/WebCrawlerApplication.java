package cn.eastseven.webcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class WebCrawlerApplication implements CommandLineRunner {

    @Autowired
    WebCrawler webCrawler;

    public static void main(String[] args) {
        SpringApplication.run(WebCrawlerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        webCrawler.startChinaPub();
        webCrawler.startWinXuan();
    }

}
