package cn.eastseven.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * Created by dongqi on 17/5/25.
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    MyFirstFilter firstFilter;

    @Autowired
    MySecondFilter secondFilter;

    @Bean
    public FilterRegistrationBean filter1() {
        log.debug(" ===== My A Filter Register =====");
        FilterRegistrationBean bean = new FilterRegistrationBean(firstFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public FilterRegistrationBean filter2() {
        log.debug(" ===== My B Filter Register =====");
        FilterRegistrationBean bean = new FilterRegistrationBean(secondFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return bean;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // http.addFilterBefore(myFilter, OAuth2AuthenticationProcessingFilter.class);
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }
}
