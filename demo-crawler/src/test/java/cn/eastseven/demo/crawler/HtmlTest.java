package cn.eastseven.demo.crawler;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

@Slf4j
public class HtmlTest {

    @Test
    public void test() {
        log.debug(">>>> test");

        String html = "PHP是世界上最好的语言，哈哈哈";
        Set<String> keywords = Sets.newHashSet("PHP", "世界", "语言");
        boolean bln = false;
        for (String keyword : keywords) {
            if (StringUtils.contains(html, keyword)) {
                bln = true;
                html = StringUtils.replaceAll(html, keyword, "<div style='color: red;'>"+keyword+"</div>");
            }
        }

        log.debug(">>> {}", html);
        Assert.assertTrue(bln);
    }
}
