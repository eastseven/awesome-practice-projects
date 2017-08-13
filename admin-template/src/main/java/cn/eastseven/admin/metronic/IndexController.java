package cn.eastseven.admin.metronic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("metronic")
public class IndexController {

    @GetMapping("/{page}.html")
    public String index(@PathVariable String page) {
        return "metronic/" + page;
    }

}
