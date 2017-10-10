package cn.eastseven.webcrawler.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data
@Slf4j
@TargetUrl("http://product.china-pub.com/\\d+")
@HelpUrl("http://www.china-pub.com/*")
@SeedUrl("http://www.china-pub.com/")
@Document(collection = "book_china_pub")
public class ChinaPub implements AfterExtractor {

    @Id
    private String url;

    @ExtractBy(value = "//div[@class='pro_book']/h1/text()", notNull = true)
    private String name;

    @ExtractBy(value = "//span[@class='pro_buy_pri']/text()", notNull = true)
    private String originPrice;

    @ExtractBy(value = "//span[@class='pro_buy_sen']/text()", notNull = true)
    private String price;

    //@ExtractBy(value = "//div[@id='con_a_1']/div[@class='pro_r_deta']/ul/li/strong/text()")
    private String isbn;

    private String image;

    private String info;

    private String contents;

    private String postDate; //上架时间：2016-11-8

    private String publishDate; //出版日期 2017 年1月

    private String originName;

    private String translator; // 译者

    private String author;

    private String press; //出版社

    @Override
    public void afterProcess(Page page) {
        Element body = page.getHtml().getDocument().body();
        this.url = page.getUrl().get();
        this.image = body.select("div.pro_book_img img").attr("src");

        try {
            Element contentTag = body.select("div#con_a_1 div.pro_r_deta h3#ml").first().siblingElements().first();
            this.contents = contentTag.html();
        } catch (Exception e) {
            log.error("", e);
        }

        this.info = body.select("#con_a_1 > div:nth-child(1) > ul").html();
        Elements elements = body.select("#con_a_1 > div:nth-child(1) > ul > li");
        for (Element li : elements) {
            String text = li.text();
            if (text.contains("原书名：")) {
                this.originName = StringUtils.remove(text, "原书名：");
            }

            if (text.contains("译者：")) {
                this.translator = StringUtils.remove(text, "译者：");
            }

            if (text.contains("作者：")) {
                this.author = StringUtils.remove(text, "作者：");
            }

            if (text.contains("出版社：")) {
                this.press = StringUtils.remove(text, "出版社：");
            }

            if (text.contains("上架时间")) {
                this.postDate = StringUtils.remove(text, "上架时间：");
            }

            if (text.contains("出版日期：")) {
                this.publishDate = StringUtils.remove(text, "出版日期：");
            }

            if (text.contains("ISBN：")) {
                this.isbn = StringUtils.remove(text, "ISBN：");
            }
        }
    }
}
