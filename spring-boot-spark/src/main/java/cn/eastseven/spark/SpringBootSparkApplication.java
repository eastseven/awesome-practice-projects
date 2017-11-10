package cn.eastseven.spark;

import cn.eastseven.spark.dao.BiddingArticle;
import cn.eastseven.spark.dao.BiddingArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootSparkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSparkApplication.class, args);
    }

    @Autowired
    BiddingArticleRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            BiddingArticle article = new BiddingArticle();
            article.setRowKey("#");
            article.setTitle("#");
            article.setIndustry("#");
            article.setCode("#");
            article.setUrl("#");
            repository.save(article);
        }
    }
}
