package cn.eastseven.webcrawler.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data
@Slf4j
@TargetUrl("http://item.winxuan.com/\\d+")
@HelpUrl("http://www.winxuan.com/")
public class WinXuan implements AfterExtractor {

    static final String PREFIX = "//div[@id='page']/div/div[@class='col-main']/div[@class='main-wrap']/div[@class='module']/div[@class='main-detail']/div[@class='cell-detail']/div[@class='info-main']";

    private String url;

    @ExtractBy(value = PREFIX + "/div[@class='name']/h1/text()")
    private String name;

    @ExtractBy(value = PREFIX + "/div[@class='attr']/dl[@class='price-o']/dd/b/text()")
    private String originPrice;

    @ExtractBy(value = PREFIX + "/div[@class='attr']/dl[@class='price-n']/dd/b/text()")
    private String price;

    private String isbn;

    private String image;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();

        Element li = page
                .getHtml()
                .getDocument()
                .body()
                .select("div.module div.unit div.col.col-base-2 div.cont ul.list-text-3.cf li")
                .last();

        this.isbn = li.text().replace("I S B N：", "");

        this.image = page.getHtml().getDocument().body().select("div.info-side div.img a.jqzoom").attr("href");

        //log.debug("\nurl={}\nname={}\nop={},p={}\nisbn={}\n\n", this.url, this.name, this.originPrice, this.price, this.isbn);
    }
}
