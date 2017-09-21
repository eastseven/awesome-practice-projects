package cn.eastseven.webcrawler;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;

@Slf4j
public class KanMeiJuTests {

    @Test
    public void test() throws Exception {
        String[] urls = {
                //生活大爆炸
                "http://www.kmeiju.net/archives/4423.html",
                "http://www.kmeiju.net/archives/3757.html",
                "http://www.kmeiju.net/archives/34.html",
                "http://www.kmeiju.net/archives/31.html",
                "http://www.kmeiju.net/archives/28.html",
                "http://www.kmeiju.net/archives/25.html",
                "http://www.kmeiju.net/archives/22.html",
                "http://www.kmeiju.net/archives/18.html",
                "http://www.kmeiju.net/archives/15.html",
                "http://www.kmeiju.net/archives/11.html",
                //NCIS
                "http://www.kmeiju.net/archives/4630.html",
                "http://www.kmeiju.net/archives/4142.html",
                "http://www.kmeiju.net/archives/2410.html",
                "http://www.kmeiju.net/archives/2408.html",
                "http://www.kmeiju.net/archives/2406.html",
                "http://www.kmeiju.net/archives/2404.html",
                //实习医生格蕾
                "http://www.kmeiju.net/archives/4462.html",
                "http://www.kmeiju.net/archives/4154.html",
                "http://www.kmeiju.net/archives/501.html",
                //警察世家
                //"http://www.kmeiju.net/archives/4200.html",
                //妙探双姝 Rizzoli & Isles
                "http://www.kmeiju.net/archives/4292.html",
                "http://www.kmeiju.net/archives/989.html",
                "http://www.kmeiju.net/archives/986.html",
                "http://www.kmeiju.net/archives/983.html",
                "http://www.kmeiju.net/archives/980.html",
                "http://www.kmeiju.net/archives/977.html",
                "http://www.kmeiju.net/archives/974.html",
                //
        };

        for (String url : urls) {
            Assert.assertNotNull(url);
            log.debug("{}", url);
            Element body = Jsoup.parse(new URL(url), 10000).body();

            String name = body.select("#art_main1 > div.art_title > h1").text();
            log.debug("{}", name);

            String content = "";
            content += name + '\t' + url + '\n';
            Element table = body.select("#content > table.table").first();
            for (Element element : table.select("tr")) { //#content > table:nth-child(9) > tbody > tr:nth-child(2) > td:nth-child(1)
                //log.debug("{}\n{}", name, element);
                content += element.select("td:nth-child(1) a").attr("href") + '\n';
            }

            if (StringUtils.isNoneBlank(content)) {
                Files.write(content.getBytes(), Paths.get("target", name + ".txt").toFile());
            } else {
                log.warn("{} content is empty, {}", name, url);
            }
        }
    }
}
