package cn.eastseven.crud.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class DashboardController {

    @GetMapping(value = {"/", "/dashboard.html"})
    public String index() {
        return "dashboard";
    }

}
