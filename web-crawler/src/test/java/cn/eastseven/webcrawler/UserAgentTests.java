package cn.eastseven.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

@Slf4j
public final class UserAgentTests {

    @Test
    public void test() throws Exception {
        String url = "http://useragentstring.com/pages/useragentstring.php?name=All";
        Elements elements = Jsoup.parse(new URL(url), 10000).body().select("div#liste ul li a");
        Assert.assertNotNull(elements);
        elements.stream().forEach(element -> System.out.println("\"" + element.text() + "\","));
    }
}
