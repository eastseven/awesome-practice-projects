package cn.eastseven.web.crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.data.hadoop.store.output.TextFileWriter;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebCrawlerWithHdfsApplicationTests {

    @Value("${app.hadoop.dir}")
    String basePath;

    @Test
    public void contextLoads() {
        log.info("{}", basePath);

        Assert.assertNotNull(basePath);

        try {
            Path path = new Path(basePath + "/data"/* + UUID.randomUUID().toString()*/);
            Configuration config = new Configuration();
            TextFileWriter writer = new TextFileWriter(config, path, null);
            writer.write(Jsoup.connect("http://www.ggzy.gov.cn/information/html/a/520000/0202/201708/18/0052aed682a2c5384aa580bb04d986e51901.shtml").get().html());
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Autowired
    FsShell fsShell;

    @Value("${spring.hadoop.fs-uri}") String uri;

    @Test
    public void testDelete() {
        for (FileStatus fileStatus : fsShell.ls("/")) {
            log.debug("删除前 {}", fileStatus.getPath());
        }

        //fsShell.rmr(uri + "/jd");
        //fsShell.rmr(uri + "/d7");
        //fsShell.rmr(uri + "/user");

        for (FileStatus fileStatus : fsShell.ls("/")) {
            log.debug("删除后 {}", fileStatus.getPath());
        }
    }

}
