package cn.eastseven.webcrawler.processor;

import cn.eastseven.webcrawler.model.UrlMetadata;
import cn.eastseven.webcrawler.repository.UrlMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;
import java.util.List;


@Slf4j
@Component
public class UrlCollectionPageProcessor implements PageProcessor {

    @Autowired
    UrlMetadataRepository dao;

    @Override
    public void process(Page page) {
        Integer level = (Integer) page.getRequest().getExtra("level");
        String filter = (String) page.getRequest().getExtra("filter");
        String rootUrl = (String) page.getRequest().getExtra("root");

        if (level > 2) return;
        log.debug("=== level {} ===", level);

        String url = page.getUrl().toString();
        //log.debug("{}", url);

        Date date = new Date();
        Elements links = page.getHtml().getDocument().body().select("a");
        List<UrlMetadata> urlMetadataList = Lists.newArrayList();
        List<Request> requestList = Lists.newArrayList();
        for (Element link : links) {
            String href = link.attr("href");

            if (href.contains("javascript")) continue;
            if (href.equals("#")) continue;
            if (!href.contains(filter)) continue;
            if (!href.startsWith("http")) href = "http:" + href;

            if (!url.equalsIgnoreCase(rootUrl) && dao.exists(href)) continue;

            UrlMetadata urlMetadata = UrlMetadata.builder()
                    .url(href)
                    .text(StringUtils.strip(link.text()))
                    .origin(url)
                    .createTime(date)
                    .level(level + 1)
                    .build();

            urlMetadataList.add(urlMetadata);

            Request request = new Request(href);
            request.putExtra(UrlMetadata.class.getName(), urlMetadata);
            request.putExtra("level", Integer.valueOf(urlMetadata.getLevel()));
            request.putExtra("filter", filter);
            requestList.add(request);

        }

        requestList.forEach(request -> page.addTargetRequest(request));
        page.putField("urlMetadataList", urlMetadataList);
        log.debug(" === put field {}", urlMetadataList.size());
    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(5000).setRetryTimes(2).setRetrySleepTime(1234).setTimeOut(5000);
    }
}
