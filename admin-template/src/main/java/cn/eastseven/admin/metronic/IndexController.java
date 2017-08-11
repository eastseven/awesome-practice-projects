package cn.eastseven.admin.metronic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("metronic")
public class IndexController {

    @GetMapping(value = {"index", "index.html"})
    public String index() {
        return "metronic/index";
    }
}
