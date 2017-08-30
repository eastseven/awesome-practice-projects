package cn.eastseven.web.crawler.processor;

import cn.eastseven.web.crawler.model.Bidding;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Component
public class GongGongZiYuanPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        log.debug("{}", url);

        if (url.startsWith("http://www.ggzy.gov.cn/information/html/")) {
            boolean hasExtrasData = page.getRequest().getExtras().containsKey(Bidding.class.getName());
            // 详情
            log.debug("{}", hasExtrasData);
            Bidding bidding = (Bidding) page.getRequest().getExtra(Bidding.class.getName());
            bidding.setHtml(page.getHtml().getDocument().html());

            page.putField(Bidding.class.getName(), bidding);
        } else {
            // 列表
            Elements elements = page.getHtml().getDocument().body().select("div.publicont");
            for (Element element : elements) {
                String href  = element.select("div h4 a").attr("href");
                String title = element.select("div h4 a").attr("title");
                String date  = element.select("div h4 span").text();
                Elements spanList = element.select("div p.p_tw span.span_on");
                String province = spanList.get(0).text();
                String origin   = spanList.get(1).text();
                String type1    = spanList.get(2).text();
                String type2    = spanList.get(3).text();

                Bidding bidding = Bidding.builder()
                        .url(href).title(title).date(date).province(province).origin(origin).type1(type1).type2(type2)
                        .build();
                log.debug("{}, {}", bidding, element);

                page.addTargetRequest(new Request(href).putExtra(Bidding.class.getName(), bidding));
            }

        }

    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
