package cn.eastseven.web.crawler.pipeline;

import cn.eastseven.web.crawler.HDFSService;
import cn.eastseven.web.crawler.model.JingDong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

import java.io.IOException;

@Slf4j
@Component
public class JingDongPipeline implements PageModelPipeline<JingDong> {

    @Autowired
    FsShell fsShell;

    @Value("${spring.hadoop.fs-uri}")
    String fsUri;

    @Autowired
    HDFSService hdfsService;

    @Override
    public void process(JingDong jingDong, Task task) {
        log.debug("{}, {}, {}", jingDong.getUrl(), jingDong.getPageName(), jingDong.getHtml().length());

        String folder = fsUri + "/jd";
        boolean exists = fsShell.test(folder);
        if (!exists) {
            fsShell.mkdir(folder);
        }

        String uri = folder + "/" + jingDong.getPageName();
//        if (fsShell.test(uri)) {
//            fsShell.rm(uri);
//        }

        String file = "target/" + jingDong.getPageName();
        try {
            hdfsService.createFile(file, jingDong.getHtml());
        } catch (IOException e) {
            log.error("", e);
        }

        try {
            hdfsService.copyFile(file, folder);
            log.debug("create {} and upload to {}", file, uri);
        } catch (IOException e) {
            log.error("", e);
        }

        int total = fsShell.ls(folder + "/*.html").size();
        log.info("total files {}", total);
    }
}