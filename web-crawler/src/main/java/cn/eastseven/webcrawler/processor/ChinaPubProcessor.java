package cn.eastseven.webcrawler.processor;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ChinaPubProcessor implements PageProcessor {

    final Pattern pattern = Pattern.compile("http://product.china-pub.com/\\d+");

    @Override
    public Site getSite() {
        return SiteUtil.get();
    }

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();

        Element body = page.getHtml().getDocument().body();
        Elements links = body.select("a");
        for (Element link : links) {
            String href = link.attr("href");
            String title = link.hasAttr("title") ? link.attr("title") : link.text();
            Matcher matcher = pattern.matcher(href);
            if (matcher.matches()) {
                log.debug(">>> {}, {}", href, title);
                page.addTargetRequest(href);
            }
        }

        if (pattern.matcher(url).matches()) {
            ChinaPub chinaPub = new ChinaPub();
            String name = body.select("#right > div.pro_book > div.pro_book > h1").text();
            String price = body.select("span.pro_buy_sen").text();
            String originPrice = body.select("span.pro_buy_pri").text();

            chinaPub.setName(name);
            chinaPub.setPrice(price);
            chinaPub.setOriginPrice(originPrice);
            chinaPub.afterProcess(page);

            log.debug("\n{}, {}\n", chinaPub, chinaPub.getIsbn());
        }
    }

}
