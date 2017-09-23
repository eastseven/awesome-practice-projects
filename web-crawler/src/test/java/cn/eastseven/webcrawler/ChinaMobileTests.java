package cn.eastseven.webcrawler;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

@Slf4j
public class ChinaMobileTests {

    @Test
    public void test() throws Exception {
        String url = "https://b2b.10086.cn/b2b/main/listVendorNoticeResult.html?noticeType=2";
        Assert.assertNotNull(url);

        Map<String, String> postParams = Maps.newHashMap();
        postParams.put("page.currentPage", "1");
        postParams.put("page.perPageSize", "20");
        postParams.put("noticeBean.sourceCH", "");
        postParams.put("noticeBean.source", "");
        postParams.put("noticeBean.title", "");
        postParams.put("noticeBean.startDate", "");
        postParams.put("noticeBean.endDate", "");

        Document doc = Jsoup.connect(url).data(postParams).post();
        Assert.assertNotNull(doc);

        Element body = doc.body();
        log.debug("\n{}", body);

        //https://b2b.10086.cn/b2b/main/viewNoticeContent.html?noticeBean.id=389913
        Elements rows = body.select("table tr");
        for (Element row : rows) {
            if (!row.hasAttr("onclick")) continue;
            String onclick = row.attr("onclick");
            String id = StringUtils.remove(onclick, "selectResult('").replace("')", "");
            String link = "https://b2b.10086.cn/b2b/main/viewNoticeContent.html?noticeBean.id=" + id;
            Elements content = Jsoup.connect(link).get().body().select("table.zb_table");
            log.debug("\n{}", content);
        }
    }
}
