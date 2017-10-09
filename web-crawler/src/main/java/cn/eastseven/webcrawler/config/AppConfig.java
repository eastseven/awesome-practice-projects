package cn.eastseven.webcrawler.config;


import cn.eastseven.webcrawler.downloader.WebDriverDownloader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling@Order(value=1)
public class AppConfig implements SchedulingConfigurer, AsyncConfigurer, CommandLineRunner {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("crawler-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

    @Bean
    WebDriverDownloader webDriverDownloader() {
        load();

        WebDriverDownloader downloader = new WebDriverDownloader();
        return downloader;
    }

    @Value("${crawler.libs.phantomjs}")
    String PATH_PHANTOMJS;

    @Value("${crawler.libs.selenium}")
    String PATH_CHROME;

    void load() {
        String chromeDriverPath = "", phantomjsPath = "";
        try {
            Properties props = System.getProperties(); //获得系统属性集
            String osName = props.getProperty("os.name"); //操作系统名称
            String os = StringUtils.lowerCase(osName);
            if (os.contains("win")) {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "windows") + ".exe";
                phantomjsPath = StringUtils.replace(PATH_PHANTOMJS, "#", "windows") + ".exe";
            } else if (os.contains("mac")) {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "macosx");
                phantomjsPath = StringUtils.replace(PATH_PHANTOMJS, "#", "macosx");
            } else {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "linux");
                phantomjsPath = StringUtils.replace(PATH_PHANTOMJS, "#", "linux");
            }
        } catch (Exception e) {
            log.error("", e);
        }

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverPath);
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjsPath);

        log.info("{}", chromeDriverPath);
        log.info("{}", phantomjsPath);
    }

    @Override
    public void run(String... strings) throws Exception {
        load();
    }
}
