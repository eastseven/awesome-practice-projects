package cn.eastseven;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by dongqi on 17/6/12.
 */
@Slf4j
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

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter() {
            @Override
            protected void init(ObjectMapper objectMapper) {
                System.out.println("===== HttpMessageConverters =====");
                super.init(objectMapper);
            }

            @Override
            public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                log.debug(" ===== read ===== {}, {}, {}", type, contextClass, inputMessage);
                Object readValue = super.read(type, contextClass, inputMessage);
                log.debug("{}", readValue);
                return readValue;
            }

            @Override
            protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                log.debug(" ===== readInternal ===== {}, {}", clazz, inputMessage);
                return super.readInternal(clazz, inputMessage);
            }

            @Override
            protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
                log.debug(" ===== writeInternal ===== {}, {}, {}", object, type, outputMessage);
                super.writeInternal(object, type, outputMessage);
            }
        };
        return new HttpMessageConverters(additional);
    }
}
