package cn.eastseven.web.crawler.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data@Slf4j
@TargetUrl("http://www.qixin.com/company/\\w+-\\w+-\\w+-\\w+-\\w+")
@HelpUrl("http://www.qixin.com/")
public class QiXin implements AfterExtractor {

    @ExtractBy(type = ExtractBy.Type.Css, value = "div.c-head div.container div.row h3.font-white")
    private String name;

    private String url;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
        System.out.println(this.name);
    }

}
