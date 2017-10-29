package cn.eastseven.webcrawler.scheduler;

import cn.eastseven.webcrawler.service.WebCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"dev", "test", "prod"})
public class SpiderMonitor {

    @Autowired
    WebCrawler webCrawler;

    @Scheduled(initialDelay = 10 * 1000, fixedRate = 60 * 1000)
    public void show() {
        StringBuilder info = new StringBuilder();
        info.append('\n').append(" ===== start ===== ").append('\n');

        try {
            webCrawler.getSpiderList().forEach(spider -> {
                info.append("\tid=" + spider.getUUID()).append('\t')
                        .append("status=" + spider.getStatus()).append('\t')
                        .append("pageCount=" + spider.getPageCount()).append('\t')
                        .append("threadAlive=" + spider.getThreadAlive()).append('\t')
                        .append('\n');
            });

        } catch (Exception e) {
            log.error("", e);
        }
        info.append('\n').append(" ===== end ===== ").append('\n');
        log.info("\n\n{}\n\n", info.toString());

        //System.out.println("\n\n" + info.toString() + "\n\n");
    }
}