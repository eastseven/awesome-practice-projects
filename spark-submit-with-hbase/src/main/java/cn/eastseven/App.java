package cn.eastseven;

import cn.eastseven.config.HBaseConfig;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 * @author eastseven
 */
@Log4j
public class App {

    public static void main(String[] args) throws IOException {
        HBaseConfig hbaseConfig = new HBaseConfig();

        Configuration conf = hbaseConfig.getHBaseConfig();
        FileSystem fs = FileSystem.get(URI.create("hdfs://s97:9000"), conf);

        List<String> titles = Lists.newArrayList();
        FileStatus[] fileStatuses = fs.listStatus(new Path("/data/bid_news_original_history/title/171121205532"));
        for (FileStatus fileStatus : fileStatuses) {
            log.info("isFile=" + fileStatus.isFile() + ", " + fileStatus.getPath());
            if (StringUtils.startsWith(fileStatus.getPath().getName(), "part-")) {
                FSDataInputStream in = fs.open(fileStatus.getPath());
                titles.addAll(IOUtils.readLines(in, "utf-8"));
                in.close();

            }

        }

        Set<String> distinct = titles.stream().distinct().collect(Collectors.toSet());
        log.info("\t>>>titles=" + titles.size() + ", distinct=" + distinct.size());

        String maxTitle = distinct.stream().max(Comparator.comparing(title -> title.length())).get();
        log.info("\t>>>" + maxTitle + ", len=" + maxTitle.length());

        fs.close();
    }
}
