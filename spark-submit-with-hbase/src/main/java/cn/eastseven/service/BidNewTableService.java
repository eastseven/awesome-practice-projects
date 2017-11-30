package cn.eastseven.service;

import cn.eastseven.config.HBaseConfig;
import lombok.extern.log4j.Log4j;

@Log4j
public class BidNewTableService {

    private HBaseConfig config;

    public BidNewTableService() {
        this.config = new HBaseConfig();
    }

    public HBaseConfig getConfig() {
        return this.config;
    }
}