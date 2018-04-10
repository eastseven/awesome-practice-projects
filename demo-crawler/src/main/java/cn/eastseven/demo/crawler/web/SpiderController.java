package cn.eastseven.demo.crawler.web;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import cn.eastseven.demo.crawler.service.SpiderService;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author eastseven
 */
@Slf4j
@RestController
@RequestMapping(value = "/spider", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "spider", tags = "爬虫配置", description = " ")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @ApiOperation(value = "爬虫启动", notes = " ")
    @GetMapping
    public Object start() {
        spiderService.start();
        return ResponseEntity.ok().body("start");
    }

    @ApiOperation(value = "添加抓取配置", notes = " ")
    @PostMapping
    public Object add(@RequestBody SpiderConfig config) {
        log.debug("\n{}", JSON.toJSONString(config, true));
        return ResponseEntity.ok(spiderService.add(config));
    }
}
