package cn.eastseven.web.crawler.processor;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Component
public class JingDongPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        Elements links = page.getHtml().getDocument().body().select("a");
        links.stream().forEach(link -> {
            String url = link.attr("href");
            String title = link.text();
            if (!url.contains("www.jd.com") && !url.contains("javascript")) {
                log.debug("{}={}", title, url);
            }
        });
    }

    @Override
    public Site getSite() {
        return Site.me().setTimeOut(100000);
    }
}
