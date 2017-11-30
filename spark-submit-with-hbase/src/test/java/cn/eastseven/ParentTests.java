package cn.eastseven;

import cn.eastseven.config.HBaseConfig;
import cn.eastseven.config.SparkConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

@Slf4j
public class ParentTests {

    protected JavaSparkContext jsc;

    protected SparkConfig config;

    private HBaseConfig conf;

    protected final byte[] f = Bytes.toBytes("f");
    protected final byte[] title = Bytes.toBytes("title");
    protected final byte[] name = Bytes.toBytes("name");

    @Before
    public void setup() {
        config = new SparkConfig("local");
        jsc = config.getJavaSparkContext();
        conf = new HBaseConfig();
    }

    @After
    public void tearDown() {
        if (jsc != null) jsc.close();
    }

    protected JavaPairRDD<ImmutableBytesWritable, Result> loadData(TableName tableName, Scan scan) throws IOException {
        Configuration hConf = conf.createNew(tableName, scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass);
        return rdd;
    }
}
