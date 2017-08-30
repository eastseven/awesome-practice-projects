package cn.eastseven.webcrawler.processor;

import cn.eastseven.webcrawler.model.Category;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Slf4j
@Component
public class CategoryProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        final String from = page.getUrl().get();
        List<Category> categoryList = Lists.newArrayList();
        page.getHtml()
                .getDocument().body()
                .select("a").stream().filter(a -> !a.attr("href").isEmpty())
                .forEach(a -> {
                    String url = a.attr("href");
                    if (from.contains("winxuan") && !url.contains("list.winxuan.com")) {
                        return;
                    }

                    categoryList.add(
                            Category.builder()
                                    .from(from).name(a.text()).url(a.attr("href"))
                                    .build());
                });

        page.getResultItems().put("categoryList", categoryList);

        page.putField("", "");
    }
}