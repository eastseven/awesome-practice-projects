package cn.eastseven.webcrawler.model;

import lombok.Data;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data
@TargetUrl("https://item.jd.com/\\d+.html")
@HelpUrl("https://www.jd.com/")
public class JD implements AfterExtractor {

    private String url;

    @ExtractBy(type = ExtractBy.Type.Css, value = "div.itemInfo-wrap > div.sku-name", notNull = true)
    private String name;

    @ExtractBy(type = ExtractBy.Type.Css, value = "div.summary-price.J-summary-price > div.dd > span > span.price", notNull = true)
    private String price;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
    }
}
