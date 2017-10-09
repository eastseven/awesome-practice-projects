package cn.eastseven.webcrawler.downloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class WebDriverDownloader implements Downloader, Closeable {

    GenericObjectPool<WebDriver> pool;

    WebDriverFactory factory;

    WebDriver webDriver;

    GenericObjectPoolConfig poolConfig;

    public WebDriverDownloader() {
        poolConfig = new GenericObjectPoolConfig();
        factory = new WebDriverFactory();
        pool = new GenericObjectPool(factory, poolConfig);
    }

    @Override
    public void close() throws IOException {
        //TODO 关闭pool，并不会关掉phantomjs，后续需要重写GenericObjectPool的close方法，调用WebDriver的quit方法来关闭phantomjs
        pool.close();
    }

    @Override
    public Page download(Request request, Task task) {
        log.debug("url={}", request.getUrl());

        try {
            webDriver = pool.borrowObject();
        } catch (Exception e) {
            log.error("", e);
        }
        webDriver.get(request.getUrl());

        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);

        //pool.returnObject(webDriver);
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }
}
