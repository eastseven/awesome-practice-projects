package cn.eastseven;

import cn.eastseven.config.HBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class HBaseTests {

    private Configuration conf;

    private Connection conn;

    private Table original;

    @Before
    public void setup() throws IOException {
        HBaseConfig config = new HBaseConfig();
        conf = config.getHBaseConfig();
        conn = ConnectionFactory.createConnection(conf);
        original = conn.getTable(TableName.valueOf("bid_news", "bid_news_original"));

    }

    @After
    public void tearDown() throws IOException {
        if (conn != null) conn.close();
    }

    @Test
    public void test() throws IOException {
        Assert.assertNotNull(original);

        final byte[] f = Bytes.toBytes("f");
        final byte[] date = Bytes.toBytes("date");
        final byte[] sourceCode = Bytes.toBytes("sourceCode");
        Scan scan = new Scan();
        scan.setFilter(new SingleColumnValueFilter(f, sourceCode, CompareFilter.CompareOp.EQUAL, Bytes.toBytes("CM")));
        //scan.addColumn(f, date);
        //scan.addColumn(f, sourceCode);

        ResultScanner resultScanner = original.getScanner(scan);
        Result result = null;
        int index = 0;
        while ((result = resultScanner.next()) != null) {
            index++;
        }

        resultScanner.close();
        log.info(">>> bid_news_original CM size {}", index);
    }
}
