package cn.eastseven.webcrawler;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Slf4j
public class HtmlTagFormatTests {

    @Test
    public void test() throws IOException {
        String url = "http://www.ggzy.gov.cn/html/b/330000/0202/201710/10/003341f5fc201f1742b2967dee9be8e17c72.shtml";
        String css = "div#mycontent div.detail_content";

        Elements content = Jsoup.connect(url).get().body().select(css).first().children();
        Assert.assertNotNull(content);

        log.debug("\n{}\n", content);
        log.debug("size {}", content.size());

        List<String> list = Lists.newArrayList();
        for (Element element : content) {
            String line = element.text();
            list.add(line);
            log.debug("{}\t{}\t{}", element.tagName(), line.length(), line);
        }

        list.forEach(System.out::println);

    }

    //递归
    @Test
    public void testRecursion() throws Exception {
        String url = "http://zhaobiao.wubaiyi.com/show-7-318052-1.html";
        String css = "#Article div.content";

        url = "http://www.chinaunicombidding.cn/jsp/cnceb/web/info1/detailNotice.jsp?id=2875703300000009139";
        css = "div#mycontent div.detail_content";

        Element root = Jsoup.connect(url).get().body().select(css).first();
        Assert.assertNotNull(root);

        System.out.println(root);

        List<String> lines = Lists.newArrayList();

        visit(root, lines);

        log.debug("{}", lines.size());

        lines.forEach(System.out::println);
    }

    public void visit(Element node, List<String> lines) {

        if (node.children().isEmpty()) return;

        for (Element child : node.children()) {
            if (child.children().isEmpty()) {
                String text = StringUtils.trim(child.text().trim());
                if (StringUtils.isBlank(text)) continue;
                log.debug(" === {}", text);
                lines.add(text);
            } else {
                visit(child, lines);
            }
        }
    }

    @Test
    public void testHtmlTagReplace() throws Exception {
        String url = "http://www.chinaunicombidding.cn/jsp/cnceb/web/info1/detailNotice.jsp?id=2875703300000009139";
        String css = "body > div.Section1";
        url = "http://www.ggzy.gov.cn/information/html/b/660000/0204/201710/11/0066424ede6b6d994efa9c01f069190693c8.shtml";
        css = "div.detail_content";

        //url = "http://empm.ccccltd.cn/PMS/biddetail.shtml?id=J8Gis7a8zFp0/0+cu62h4ufCdjRQ/t9M5buuVsVwZblCKGlOcrU5SpArq7je03IAqGcjcm4LS06Jg7x4SlJl5mGjrLcESjEnqGcjcm4LS06wunPuBDym2zkNGvz9XSmH";
        //css = "body > div > table";

        Element body = Jsoup.connect(url).get().body();
        Assert.assertNotNull(body);

        String html = body.select(css).html();
        Assert.assertNotNull(html);
        System.out.println(" ===== 原文 =====");
        System.out.println(html);

        String text = StringUtils.removeAll(html, "\\w+=\".*?\"|<!-{2,}.*?-{2,}>");
        System.out.println(" ===== 处理后 =====");
        System.out.println(text);
    }
}
