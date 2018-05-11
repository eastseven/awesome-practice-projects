package cn.eastseven.demo.crawler.service.impl;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import cn.eastseven.demo.crawler.pipeline.SpiderDataPipeline;
import cn.eastseven.demo.crawler.repository.SpiderConfigRepository;
import cn.eastseven.demo.crawler.service.HtmlPageProcessor;
import cn.eastseven.demo.crawler.service.SpiderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

/**
 * @author eastseven
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    @Autowired
    private SpiderConfigRepository repository;

    @Autowired
    private SpiderDataPipeline spiderDataPipeline;

    @Autowired
    private HtmlPageProcessor pageProcessor;

    @Override
    public void start() {
        long size = repository.count();
        if (size == 0L) {
            log.warn("没有要抓取的数据");
            return;
        }

        repository.findAll().forEach(spiderConfig -> start(spiderConfig));
    }

    void start(SpiderConfig config) {
        if (StringUtils.isBlank(config.getKeywords())) {
            log.error("keywords 不能为空", config);
        }
        Request request = new Request(config.getUrl());
        request.putExtra(config.getClass().getName(), config);
        Spider.create(pageProcessor).setScheduler(new FileCacheQueueScheduler("urls")).addRequest(request).addPipeline(spiderDataPipeline).run();
    }

    @Override
    public SpiderConfig add(SpiderConfig config) {
        boolean exists = repository.existsById(config.getUrl());
        if (!exists) {
            repository.save(config);
            new Thread(() -> {
                start(config);
            }).start();
        }

        return config;
    }
}
