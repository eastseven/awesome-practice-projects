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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Slf4j
@Configuration
@EnableAsync
@EnableScheduling
@Order(value = 1)
public class AppConfig implements SchedulingConfigurer, AsyncConfigurer, CommandLineRunner {

    @Bean
    ExecutorService executorService() {
        final int size = Runtime.getRuntime().availableProcessors() * 4;
        final int max = size * 2;
        final int cap = max * 2;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(size);
        executor.setMaxPoolSize(max);
        executor.setQueueCapacity(cap);
        executor.setThreadNamePrefix("crawler-");
        executor.initialize();
        log.info("ThreadPoolTaskExecutor param >>> core {}, max {}, cap {}", size, max, cap);
        return executor.getThreadPoolExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        final int size = Runtime.getRuntime().availableProcessors() * 4;
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(size);
        executor.setMaximumPoolSize(size * 2);
        taskRegistrar.setScheduler(executor);
    }

    @Override
    public Executor getAsyncExecutor() {
        return executorService();
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
    String PATH_PHANTOM_JS;

    @Value("${crawler.libs.selenium}")
    String PATH_CHROME;

    void load() {
        String chromeDriverPath = "";
        String phantomJsPath = "";
        try {
            //获得系统属性集
            Properties props = System.getProperties();

            //操作系统名称
            String osName = props.getProperty("os.name");

            final String OS_WIN = "win";
            final String OS_MAC = "mac";

            String os = StringUtils.lowerCase(osName);
            if (os.contains(OS_WIN)) {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "windows") + ".exe";
                phantomJsPath = StringUtils.replace(PATH_PHANTOM_JS, "#", "windows") + ".exe";
            } else if (os.contains(OS_MAC)) {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "macosx");
                phantomJsPath = StringUtils.replace(PATH_PHANTOM_JS, "#", "macosx");
            } else {
                chromeDriverPath = StringUtils.replace(PATH_CHROME, "#", "linux");
                phantomJsPath = StringUtils.replace(PATH_PHANTOM_JS, "#", "linux");
            }
        } catch (Exception e) {
            log.error("", e);
        }

        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverPath);
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsPath);

        log.info("{}", chromeDriverPath);
        log.info("{}", phantomJsPath);
    }

    @Override
    public void run(String... strings) throws Exception {
        load();
    }
}
