package cn.eastseven.webmagic.slave;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;

/**
 * @author eastseven
 */
@Slf4j
@Component
public class Receiver {

    @Autowired
    RedisScheduler redisScheduler;

    private Spider spider;

    @PostConstruct
    public void init() {
        spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                log.info(">>> html length {}", page.getHtml().getDocument().body().select("div#con_a_1").html().length());
            }

            @Override
            public Site getSite() {
                return Site.me();
            }
        });

        spider.thread(10);
        spider.setScheduler(redisScheduler);
        spider.start();
        log.info(">>> spider {}", spider.getStatus());
    }

    @PreDestroy
    public void close() {
        spider.close();
    }

    public void receiveMessage(String message) throws IOException {
        System.out.println("Received <" + message + ">");
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> data = mapper.readValue(message, Map.class);
        String url = (String) data.get("url");
        spider.addUrl(url);

        if (!spider.getStatus().equals(Spider.Status.Running)) {
            spider.start();
        }
    }
}
