package cn.eastseven.handler;

import cn.eastseven.config.HBaseConfig;
import cn.eastseven.config.SparkConfig;
import lombok.extern.log4j.Log4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author eastseven
 */
@Log4j
public class BidNewOriginalTableHandler implements Serializable {

    public static void main(String[] args) throws IOException {
        new BidNewOriginalTableHandler().jobA(new SparkConfig());
    }

    public void jobA(SparkConfig sparkConfig) throws IOException {
        JavaSparkContext jsc = sparkConfig.getJavaSparkContext();
        HBaseConfig conf = new HBaseConfig();
        final byte[] f = Bytes.toBytes("f");
        final byte[] sourceCode = Bytes.toBytes("sourceCode");
        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, sourceCode);
        scan.setFilter(new SingleColumnValueFilter(f, sourceCode, CompareFilter.CompareOp.EQUAL, Bytes.toBytes("CM")));
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_original"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;

        // (rowKey, title)
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass).distinct().cache();
        long total = rdd.count();

        JavaRDD<String> rowKeyRDD = rdd.values().map(result -> Bytes.toString(result.getRow())).cache();
        JavaRDD<String> rowKeyDistinctRDD = rowKeyRDD.distinct();
        long c = rowKeyDistinctRDD.count();
        long rowKeyCount = rowKeyRDD.count();
        String path = "target/cm_" + DateTime.now().toString("yyyyMMddHHmm");
        rowKeyRDD.saveAsTextFile(path);
        log.info(">>> bid_news_original total CM size " + total + ", row key size " + rowKeyCount + ", distinct " + c);

        jsc.close();
    }
}
