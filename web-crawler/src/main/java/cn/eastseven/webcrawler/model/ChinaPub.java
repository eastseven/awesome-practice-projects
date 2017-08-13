package cn.eastseven.webcrawler.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data@Slf4j
@TargetUrl("http://product.china-pub.com/\\d+")
@HelpUrl("http://www.china-pub.com/")
public class ChinaPub implements AfterExtractor {

    private String url;

    @ExtractBy(value = "//div[@class='pro_book']/h1/text()")
    private String name;

    @ExtractBy(value = "//span[@class='pro_buy_pri']/text()")
    private String originPrice;

    @ExtractBy(value = "//span[@class='pro_buy_sen']/text()")
    private String price;

    @ExtractBy(value = "//div[@id='con_a_1']/div[@class='pro_r_deta']/ul/li/strong/text()")
    private String isbn;

    private String image;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
        this.image = page.getHtml().getDocument().body().select("div.pro_book_img img").attr("src");

        log.debug("\n\t{}\n", this.image);
    }
}
