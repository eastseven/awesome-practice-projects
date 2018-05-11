package cn.eastseven.demo.crawler.service;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import cn.eastseven.demo.crawler.model.SpiderData;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author eastseven
 */
@Slf4j
@Component
public class HtmlPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        if (page.getRequest() != null && page.getRequest().getExtra(SpiderConfig.class.getName()) != null) {
            SpiderConfig config = (SpiderConfig) page.getRequest().getExtra(SpiderConfig.class.getName());
            final Pattern pattern = Pattern.compile(config.getRegExp());
            List<Element> elements = page.getHtml().getDocument().body().select("a")
                    .stream().filter(element -> pattern.matcher(element.attr("href")).find())
                    .collect(Collectors.toList());

            elements.forEach(element -> {
                SpiderData data = SpiderData.builder().url(element.attr("href")).config(config).build();
                if (StringUtils.isNoneBlank(element.text())) {
                    data.setTitle(element.text());
                }
                Request req = new Request(data.getUrl());
                req.putExtra(data.getClass().getName(), data);
                page.addTargetRequest(req);
            });
        }

        if (page.getRequest() != null && page.getRequest().getExtra(SpiderData.class.getName()) != null) {
            SpiderData data = (SpiderData) page.getRequest().getExtra(SpiderData.class.getName());
            SpiderConfig config = data.getConfig();

            Set<String> keywords = Sets.newHashSet(config.getKeywords().split(" "));

            final Pattern pattern = Pattern.compile(config.getRegExp());
            String url = page.getUrl().toString();
            if (pattern.matcher(url).find()) {
                Elements elements = page.getHtml().getDocument().body().select(config.getTargetElement());
                String html = Jsoup.clean(elements.html(), Whitelist.basicWithImages());

                boolean bln = false;
                for (String keyword : keywords) {
                    if (StringUtils.contains(html, keyword)) {
                        bln = true;
                        html = StringUtils.replaceAll(html, keyword, "<div style='color: red;'>"+keyword+"</div>");
                    }
                }

                if (bln) {
                    data.setContent(StringUtils.strip(html));
                    data.setCreateTime(DateTime.now().toDate());

                    log.debug("\n{}", JSON.toJSONString(data, true));

                    page.getResultItems().put(data.getClass().getName(), data);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}
