package cn.eastseven.rbac.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by dongqi on 17/5/23.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");

        registry.addViewController("/500").setViewName("500");
        registry.addViewController("/404").setViewName("404");
        registry.addViewController("/403").setViewName("403");
    }
}
