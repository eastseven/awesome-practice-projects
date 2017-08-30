package cn.eastseven.web.crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.fs.FsShell;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class HDFSOperationTests extends WebCrawlerWithHdfsApplicationTests {

    @Autowired
    FsShell fsShell;

    @Value("${spring.hadoop.fs-uri}")
    String fsUri;

    @Test
    public void testWrite() throws IOException {
        for (FileStatus fileStatus : fsShell.ls("/*")) {
            log.debug(" ===== {}", fileStatus.getPath());
        }

        Configuration conf = new Configuration();
        conf.set("fs.default.name", fsUri);

        FileSystem fileSystem = FileSystem.get(URI.create("/jd"), conf);
        Path src = new Path("target/test.txt");
        FSDataOutputStream out = fileSystem.create(src);
        out.write("hello world\n中文测试".getBytes("utf-8"));
        IOUtils.closeStream(out);
        log.debug(" ===== local {}, {}", src, fileSystem.exists(src));

        Path dst = new Path(fsUri + "/jd");
        log.debug(" ===== remote {}, {}", dst, fsShell.test(dst.toUri().toString()));
        if (!fsShell.test(dst.toUri().toString())) fsShell.touchz(dst.toUri().toString());

        fileSystem.copyFromLocalFile(false, true, src, dst);
        log.debug(" ===== {}, {}", src, fileSystem.exists(src));

        fileSystem.close();
    }

    @Test
    public void testRead() {

    }
}
