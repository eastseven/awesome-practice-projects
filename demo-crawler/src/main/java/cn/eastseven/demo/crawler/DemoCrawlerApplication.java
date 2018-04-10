package cn.eastseven.demo.crawler;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author eastseven
 */
@EnableSwagger2Doc
@SpringBootApplication
public class DemoCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCrawlerApplication.class, args);
    }
}
