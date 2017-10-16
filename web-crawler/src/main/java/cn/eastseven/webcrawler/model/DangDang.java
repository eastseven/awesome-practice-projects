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
@TargetUrl("http://product.dangdang.com/\\d+.html")
@HelpUrl("http://book.dangdang.com/*")
@SeedUrl("http://book.dangdang.com/")
@Document(collection = "book_dang_dang")
public class DangDang extends BaseBook implements AfterExtractor {

    @Id
    private String url;

    private String name;

    @ExtractBy(value = "//*[@id=\"original-price\"]/text()", notNull = true)
    private String originPrice;

    @ExtractBy(value = "//*[@id=\"dd-price\"]/text()", notNull = true)
    private String price;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
        this.name = page.getHtml().getDocument().body().select("div.name_info > h1").text();
        this.image = page.getHtml().getDocument().body().select("div#largePicDiv > a > img").attr("src");
        for (Element element : page.getHtml().getDocument().body().select("#detail_describe > ul > li")) {
            String text = element.text();
            if (StringUtils.contains(text, "ISBN")) {
                this.isbn = StringUtils.remove(text, "国际标准书号ISBN：");
            }

            if (StringUtils.contains(text, "印刷时间：")) {
                this.publishDate = StringUtils.remove(text, "印刷时间：");
            }

            if (StringUtils.contains(text, "所属分类：")) {
                this.doCategory(element.select("span a.green"), BookOrigin.DANG_DANG);
            }
        }

        this.info = page.getHtml().getDocument().body().select("#detail_describe").html();

        this.contents = page.getHtml().getDocument().body().select("textarea#catalog-textarea").html();

        Elements elements = page.getHtml().getDocument().body().select("div.sale_box_left > div.messbox_info > span");
        for (Element span : elements) {
            String text = span.text();
            if (text.contains("作者")) {
                this.author = StringUtils.remove(text, "作者:");
            }

            if (text.contains("出版社")) {
                this.press = StringUtils.remove(text, "出版社:");
            }
        }
    }
}
