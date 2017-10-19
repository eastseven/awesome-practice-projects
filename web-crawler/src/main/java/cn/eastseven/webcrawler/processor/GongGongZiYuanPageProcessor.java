package cn.eastseven.webcrawler.processor;

import cn.eastseven.webcrawler.model.GongGongZiYuanStatistics;
import cn.eastseven.webcrawler.utils.SiteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 全国公共资源交易平台
 * http://deal.ggzy.gov.cn/ds/deal/dealList.jsp
 *
 * @author dongqi
 */
@Slf4j
@Component
public class GongGongZiYuanPageProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        String date = (String) page.getRequest().getExtra("date");
        log.debug("{}, {}", date, url);

        Elements totalSize = page.getHtml().getDocument().body().select("div#publicl div.contp span.span_left:nth-child(1)");
        Elements totalPage = page.getHtml().getDocument().body().select("div#publicl div.contp span.span_right");

        log.debug("{}", totalSize.select("b").text());
        log.debug("{}", totalPage.select("b").text());

        GongGongZiYuanStatistics data = new GongGongZiYuanStatistics();
        data.setDatetime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        data.setDate(date);
        data.setTotalSize(Long.valueOf(totalSize.select("b").text()));
        data.setTotalPage(Long.valueOf(StringUtils.substringAfter(totalPage.select("b").text(), "/")));
        log.debug("{}", data);

        page.putField(GongGongZiYuanStatistics.class.getSimpleName(), data);
    }

    @Override
    public Site getSite() {
        return SiteUtil.get();
    }
}
