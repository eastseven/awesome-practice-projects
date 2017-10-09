package cn.eastseven.webcrawler.downloader;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {

    @Override
    public WebDriver create() throws Exception {
        return new PhantomJSDriver();
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver obj) {
        return new DefaultPooledObject(obj);
    }
}
