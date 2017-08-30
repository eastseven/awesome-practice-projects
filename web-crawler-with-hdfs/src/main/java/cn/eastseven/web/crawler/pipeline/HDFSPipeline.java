package cn.eastseven.web.crawler.pipeline;

import cn.eastseven.web.crawler.model.BiddingContent;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
public class HDFSPipeline implements Pipeline {

    @Value("${app.hadoop.dir}")
    String hdfsPath;

    @Value("${app.hadoop.local.dir}")
    String localPath;

    @Value("${spring.hadoop.fs-uri}")
    String fsUri;

    @Autowired
    FsShell fsShell;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.debug(" ==== save to HDFS ==== {}", task.getUUID());
        BiddingContent biddingContent = resultItems.get(BiddingContent.class.getName());
        if (biddingContent != null) {
            log.debug("{}, {}", biddingContent.getId(), biddingContent.getHtml().length());
            try {
                createFile(biddingContent);
            } catch (IOException e) {
                log.error("", e);
            }

        }
    }

    // 本地保存
    void createFile(BiddingContent biddingContent) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(localPath), conf);
        byte[] buff = biddingContent.getHtml().getBytes();
        FSDataOutputStream os = null;
        Path src = null;
        try {
            src = new Path(localPath + biddingContent.getId());
            os = fs.create(src);
            os.write(buff, 0, buff.length);
            log.debug("Create: {}", src);

        } finally {
            if (os != null)
                os.close();
        }
        fs.close();

        if (canUpload(src)) {
            copyFile(src.toUri().getPath(), hdfsPath);
        }
    }

    boolean canUpload(Path src) {
        return src != null;
    }

    // 上传至服务器
    void copyFile(String local, String remote) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(fsUri), conf);
        Path src = new Path(local);
        Path dst = new Path(remote);
        log.debug("src={}, dst={}", src, dst);
        fs.copyFromLocalFile(src, dst);
        log.debug("copy from: {} to {}", local, remote);
        fs.close();
    }

}
