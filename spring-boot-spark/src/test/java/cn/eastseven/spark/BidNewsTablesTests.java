package cn.eastseven.spark;

import cn.eastseven.spark.config.HBaseConfig;
import cn.eastseven.spark.service.SparkHBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.EQUAL;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BidNewsTablesTests {

    @Autowired
    SparkHBaseService service;

    @Autowired
    HBaseConfig config;

    final byte[] f = Bytes.toBytes("f");
    final byte[] type = Bytes.toBytes("type");
    final byte[] title = Bytes.toBytes("title");
    final byte[] sourceCode = Bytes.toBytes("sourceCode");
    final byte[] formatContent = Bytes.toBytes("formatContent");

    @Test
    public void test() throws IOException {
        assertTrue(CollectionUtils.isNotEmpty(config.getTableNames()));

        for (TableName tableName : config.getTableNames()) {
            long total = service.getHBaseResultJavaPairRDD(tableName).count();
            log.info(">>> {} = {}", tableName.getNameWithNamespaceInclAsString(), total);
        }
    }

    @Test
    public void testTableOriginal() throws Exception {
        assertNotNull(config.getTableOriginal());

        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, formatContent);
        scan.addColumn(f, sourceCode);
        scan.addColumn(f, title);
        scan.addColumn(f, type);
        scan.setFilter(new SingleColumnValueFilter(f, type, EQUAL, new RegexStringComparator(".+招标+.")));

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> {} {}", config.getTableOriginal(), rdd.count());

        rdd.foreach(result -> {
            String typeValue = Bytes.toString(result._2.getValue(f, type));
            String titleOriginalValue = Bytes.toString(result._2.getValue(f, title));
            String sourceCodeValue = Bytes.toString(result._2.getValue(f, sourceCode));
            String formatContentValue = Bytes.toString(result._2.getValue(f, formatContent));

            String content = Jsoup.clean(formatContentValue, Whitelist.none());
            String titleValue = StringUtils.removeEnd(titleOriginalValue, typeValue);
            //log.debug(">>> {}, {}, {}[{}], {}", sourceCodeValue, typeValue, titleOriginalValue, titleValue, content.contains(titleValue));
        });

        JavaRDD<String> titleTextRDD = rdd.map(result -> StringUtils.strip(Bytes.toString(result._2.getValue(f, title))));

        final String path = "spark-warehouse/bidding_titles";
        titleTextRDD.saveAsTextFile(path);
    }

    @Test
    public void testType() throws Exception {
        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, type);
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> {} {}", config.getTableOriginal(), rdd.count());

        JavaRDD<String> typeRdd = rdd.map(row -> Bytes.toString(row._2.getValue(f, type))).distinct();
        log.debug(">>> type rdd {}", typeRdd.count());

        typeRdd.distinct().foreach(row -> log.debug(">>> distinct type {}", row));

        assertNotNull(typeRdd);
    }
}
