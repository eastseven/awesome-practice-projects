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
        String url = "http://www.ggzy.gov.cn/information/html/b/350000/0201/201709/26/00350f7009fcf67d43db8f680e6469ea984f.shtml";
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
}
