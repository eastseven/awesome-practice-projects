package cn.eastseven.webcrawler.processor;

import cn.eastseven.webcrawler.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Slf4j
@Component
public class ChinaPubProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Elements elements = page.getHtml().getDocument().body().select("div.search_result table");
        List<Book> bookList = Lists.newArrayList();
        elements.forEach(element -> {
            Elements tableRow = element.select("tr");
            log.debug("{}", tableRow);
            String url = tableRow.select("td").first().select("a").attr("href");
            String image = tableRow.select("td").first().select("img").attr("src");
            String name = tableRow.select("td").last().select("ul li.result_name a").text();
            String price = tableRow.select("td").last().select("ul li.result_book ul li.book_dis b").text();
            String originPrice = tableRow.select("td").last().select("ul li.result_book ul li.book_price").text();
            Book book = Book.builder().image(image).name(name).price(price).originPrice(originPrice).url(url).build();
            bookList.add(book);
        });

        page.getResultItems().put("bookList", bookList);
    }

}
