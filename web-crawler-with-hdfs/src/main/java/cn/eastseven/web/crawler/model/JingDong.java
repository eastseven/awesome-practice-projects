package cn.eastseven.web.crawler.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

@Data
@TargetUrl("https://item.jd.com/\\d+.html")
@HelpUrl("https://www.jd.com/newWare.html")
public class JingDong implements AfterExtractor {

    private String url;

    private String html;

    private String pageName;

    @Override
    public void afterProcess(Page page) {
        this.url = page.getUrl().get();
        this.html = page.getHtml().getDocument().html();

        if (StringUtils.isNotBlank(this.url)) {
            this.pageName = StringUtils.substringAfterLast(this.url, "/");
        }
    }
}
