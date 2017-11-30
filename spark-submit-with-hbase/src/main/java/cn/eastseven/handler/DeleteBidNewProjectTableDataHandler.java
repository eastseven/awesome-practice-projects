package cn.eastseven.handler;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author eastseven
 */
@Log4j
public class DeleteBidNewProjectTableDataHandler {

    public static void main(String[] args) throws Exception {
        log.info(">>> ");
        List<String> ids = Lists.newArrayList();
        List<String> files = Arrays.asList("part-00000", "part-00001", "part-00002", "part-00003", "part-00004");
        for (String file : files) {
            List<String> list = Files.readAllLines(Paths.get("/Users/dongqi/Downloads/invalid_same_bid_id_20171121_170337/", file));
            ids.addAll(list);
        }
        log.info(">>> size=" + ids.size());
        List<Delete> deleteList = ids.stream().map(id -> new Delete(Bytes.toBytes(id))).collect(Collectors.toList());
        log.info(">>> deleteList size=" + deleteList.size());

        Configuration hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", "s94,s95,s96");
        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseConfig.setInt("mapreduce.task.timeout", 1200000);
        hbaseConfig.setInt("hbase.client.scanner.timeout.period", 600000);
        hbaseConfig.setInt("hbase.rpc.timeout", 600000);
        Connection conn = ConnectionFactory.createConnection(hbaseConfig);
        Table projectTable = conn.getTable(TableName.valueOf("bid_news", "bid_news_project"));

        projectTable.delete(deleteList);
        log.info(">>> delete...");

        projectTable.close();
        conn.close();
        log.info(">>> done...");
    }
}
