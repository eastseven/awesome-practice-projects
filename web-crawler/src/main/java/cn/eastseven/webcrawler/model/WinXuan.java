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
@TargetUrl({"http://item.winxuan.com/\\d+", "http://www.winxuan.com/product/\\d+"})
@HelpUrl({
        "http://www.winxuan.com/*",
        "http://search.winxuan.com/*",
        "http://list.winxuan.com/*",
        "http://www.winxuan.com/catalog_book.html"
})
@SeedUrl("http://www.winxuan.com/")
@Document(collection = "book_wen_xuan")
public class WinXuan extends BaseBook implements AfterExtractor {

    static final String PREFIX = "//div[@id='page']/div/div[@class='col-main']/div[@class='main-wrap']/div[@class='module']/div[@class='main-detail']/div[@class='cell-detail']/div[@class='info-main']";

    @Id
    private String url;

    @ExtractBy(value = PREFIX + "/div[@class='name']/h1/text()", notNull = true)
    private String name;

    @ExtractBy(value = PREFIX + "/div[@class='attr']/dl[@class='price-o']/dd/b/text()", notNull = true)
    private String originPrice;

    @ExtractBy(value = PREFIX + "/div[@class='attr']/dl[@class='price-n']/dd/b/text()", notNull = true)
    private String price;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();

        Element body = page.getHtml().getDocument().body();
        Elements elements = body.select("div.module div.unit div.col.col-base-2 div.cont ul.list-text-3.cf li");

        for (Element li : elements) {
            String text = li.text();

            if (text.contains("I S B N：")) {
                this.isbn = StringUtils.remove(text, "I S B N：");
            }

            if (text.contains("出版时间：")) {
                this.publishDate = StringUtils.remove(text, "出版时间：");
            }

            if (text.contains("作　者：")) {
                this.author = StringUtils.remove(text, "作　者：");
            }

            if (text.contains("出版社：")) {
                this.press = StringUtils.remove(text, "出版社：");
            }
        }

        this.image = body.select("div.info-side div.img a.jqzoom").attr("href");

        this.contents = body.select("div.unit.book-introduce div:nth-child(1) div.text-words-1").html();

        this.info = body.select("div.col-main > div > div:nth-child(3) > div > div > div.cont > ul").html();

        this.doCategory(body.select("div.info-main > div.attr > dl.class > dd > a"), BookOrigin.WIN_XUAN);
    }
}
