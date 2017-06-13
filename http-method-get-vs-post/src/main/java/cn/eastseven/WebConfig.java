package cn.eastseven;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by dongqi on 17/6/12.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/session")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowedOrigins("http://localhost:8080")
        .allowCredentials(true)
        ;
    }
}
