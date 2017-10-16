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

        url = "http://www.ggzy.gov.cn/information/html/b/620000/0104/201710/11/00624d175de4926545eea8b7fe08e93c6f8a.shtml";
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
        url = "http://www.ggzy.gov.cn/information/html/b/410000/0201/201710/13/00417acc0b17b3da4cd6aeebd5c0b0bddff6.shtml";
        css = "div#mycontent div.detail_content";

        Element body = Jsoup.connect(url).get().body();
        Assert.assertNotNull(body);

        String html = body.select(css).html();
        Assert.assertNotNull(html);
        System.out.println(" ===== 原文 =====");
        System.out.println(html);

        //\w+=".*?"|
        String text = StringUtils.removeAll(html, "(style=\".*?\")|(width=\".*?\")|(height=\".*?\")|<!-{2,}.*?-{2,}>|(&nbsp;)");
        System.out.println(" ===== 处理后 =====");
        System.out.println(text);
    }
}
