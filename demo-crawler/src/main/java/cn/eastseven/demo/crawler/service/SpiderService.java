package cn.eastseven.demo.crawler.service;

import cn.eastseven.demo.crawler.model.SpiderConfig;

/**
 * @author eastseven
 */
public interface SpiderService {

    /**
     * 入口
     */
    void start();

    /**
     * 添加
     * @param config
     * @return SpiderConfig
     */
    SpiderConfig add(SpiderConfig config);
}
