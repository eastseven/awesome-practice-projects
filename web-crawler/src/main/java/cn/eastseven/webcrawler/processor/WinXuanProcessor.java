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
public class WinXuanProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        List<Book> bookList = Lists.newArrayList();
        Elements elements = page.getHtml().getDocument().body().select("ul#grid.list.list-group").select("div.cell.cell-m-book-top-pop");
        elements.forEach(element -> {
            log.debug("{}", element);

            String url = element.select("div.img a").attr("href");
            String image = element.select("div.img a img").attr("src");
            String name = element.select("div.name a").text();
            String price = element.select("div.price span.price-n").text();
            String originPrice = element.select("div.price span.price-o").text();

            Book book = Book.builder().image(image).name(name).price(price).originPrice(originPrice).url(url).build();
            bookList.add(book);
            log.debug("{}", book);
        });

        page.getResultItems().put("bookList", bookList);
    }

}
