package cn.eastseven.webcrawler.config;


import cn.eastseven.webcrawler.downloader.WebDriverDownloader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Properties;

@Slf4j
@Configuration
//@EnableAsync
@EnableScheduling
@Order(value = 1)
public class AppConfig implements CommandLineRunner {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        taskScheduler.setThreadNamePrefix("crawler-task-");
        log.info(">>> TaskScheduler init");
        return taskScheduler;
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
