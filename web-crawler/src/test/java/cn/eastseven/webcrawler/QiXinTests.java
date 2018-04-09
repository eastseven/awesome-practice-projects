package cn.eastseven.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

@Slf4j
public class QiXinTests {

    @Test
    public void test() throws IOException {
        DesiredCapabilities caps = DesiredCapabilities.phantomjs();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/Users/dongqi/Dev/phantomjs/2.1.1/bin/phantomjs");
        WebDriver driver = new PhantomJSDriver(caps);
        Assert.assertNotNull(driver);

        driver.get("http://c.pingan.com/apply/newpublic/credit_card_query/index.html#index");
        //driver.get("http://www.msj1.com/kanmeiju-ed2k");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(">>> 1 <<< \n"+driver.getPageSource());

        driver.findElement(By.id("idNo")).sendKeys("510107201504290162");
        driver.findElement(By.id("idNo")).sendKeys("510107201504290162");
        /*
        driver.findElement(By.id("s1-0[0]-0[0]-account")).sendKeys("17380678381");
        driver.findElement(By.id("s1-0[0]-0[0]-password")).sendKeys("dq123456");
        driver.findElement(By.cssSelector("div.form-group a.btn.btn-primary.btn-block.btn-lg")).click();

        String source = driver.getPageSource();
        log.debug("\n{}", source);

        driver.quit();
        System.out.println("==========");
        */
    }

    @Test
    public void testChrome() {
        String chromeDriver = "/Users/dongqi/Dev/code/work-har/har-sjfxpt/har-business-crawler-tianyancha/libs/selenium/mac/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        WebDriver driver = new ChromeDriver();

        Assert.assertNotNull(driver);

        driver.get("http://www.qixin.com/auth/login");

        driver.findElement(By.id("s1-0[0]-0[0]-account")).sendKeys("17380678381");
        driver.findElement(By.id("s1-0[0]-0[0]-password")).sendKeys("d12345678");
        driver.findElement(By.cssSelector("div.form-group a.btn.btn-primary.btn-block.btn-lg")).click();

        try {
            Thread.sleep(60000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.close();
    }
}
