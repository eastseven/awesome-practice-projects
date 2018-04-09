package cn.eastseven.demo.crawler.web;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import cn.eastseven.demo.crawler.service.SpiderService;
import com.alibaba.fastjson.JSON;
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
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @GetMapping
    public Object start() {
        spiderService.start();
        return ResponseEntity.ok().body("start");
    }

    @PostMapping
    public Object add(@RequestBody SpiderConfig config) {
        log.debug("\n{}", JSON.toJSONString(config, true));
        return ResponseEntity.ok(spiderService.add(config));
    }
}
