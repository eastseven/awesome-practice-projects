package cn.eastseven;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebFormSubmitApplicationTests {

    @Test
    public void contextLoads() {
        String text = DateTime.parse("2017\\.08\\.23").toString("yyyy-MM-dd HH:mm:ss");
        Assert.assertNotNull(text);
        log.debug("{}", text);
    }

}
