package cn.eastseven.webcrawler.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data
@Slf4j
@TargetUrl("http://product.dangdang.com/\\d+.html")
@HelpUrl("http://book.dangdang.com/*")
@SeedUrl("http://book.dangdang.com/")
@Document(collection = "book_dang_dang")
public class DangDang implements AfterExtractor {

    @Id
    private String url;

    private String name;

    @ExtractBy(value = "//*[@id=\"original-price\"]/text()", notNull = true)
    private String originPrice;

    @ExtractBy(value = "//*[@id=\"dd-price\"]/text()", notNull = true)
    private String price;

    //@ExtractBy(value = "", notNull = true)
    private String isbn;

    //@ExtractBy(type = ExtractBy.Type.Css, value = "div#largePicDiv > a > img")
    private String image;

    private String contents;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
        this.name = page.getHtml().getDocument().body().select("div.name_info > h1").text();
        this.image = page.getHtml().getDocument().body().select("div#largePicDiv > a > img").attr("src");
        for (Element element : page.getHtml().getDocument().body().select("#detail_describe > ul > li")) {
            if (StringUtils.contains(element.text(), "ISBN")) {
                this.isbn = StringUtils.remove(element.text(), "国际标准书号ISBN：");
                break;
            }
        }

        this.contents = page.getHtml().getDocument().body().select("textarea#catalog-textarea").html();
    }
}
