package cn.eastseven.demo.crawler.web;

import cn.eastseven.demo.crawler.repository.SpiderDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eastseven
 */
@Slf4j
@RestController
@RequestMapping(value = "/data", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DataController {

    @Autowired
    private SpiderDataRepository repository;

    @GetMapping
    public Object page(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("createTime").descending()));
    }
}
