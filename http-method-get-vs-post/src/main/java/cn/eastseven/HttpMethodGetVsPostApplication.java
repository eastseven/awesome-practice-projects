package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
//@Controller
@RestController
@SpringBootApplication
public class HttpMethodGetVsPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpMethodGetVsPostApplication.class, args);
    }

    @GetMapping("/get")
    public Object get(@RequestBody Map requestParam) {
        log.info("GET, requestParam={}", requestParam);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/post")
    public Object post(@RequestBody Map requestParam) {
        log.info("POST, requestParam={}", requestParam);
        return ResponseEntity.ok(true);
    }
}
