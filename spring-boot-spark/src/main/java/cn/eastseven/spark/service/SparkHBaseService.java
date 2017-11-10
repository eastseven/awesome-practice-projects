package cn.eastseven.spark.service;

import cn.eastseven.spark.config.HBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author eastseven
 */
@Slf4j
@Service
public class SparkHBaseService {

    @Autowired
    HBaseConfig hbaseConfig;

    private JavaSparkContext javaSparkContext;

    @PostConstruct
    public void init() {
        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SparkHBaseService")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();

        javaSparkContext = new JavaSparkContext(spark.sparkContext());
    }

    @PreDestroy
    public void destroy() {
        if (javaSparkContext != null) javaSparkContext.close();
    }

    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }

    public JavaPairRDD<ImmutableBytesWritable, Result> getHBaseResultJavaPairRDD(TableName tableName) throws IOException {
        return getHBaseResultJavaPairRDD(tableName, new Scan());
    }

    public JavaPairRDD<ImmutableBytesWritable, Result> getHBaseResultJavaPairRDD(TableName tableName, Scan scan) throws IOException {
        Configuration conf = hbaseConfig.get();
        conf.set(TableInputFormat.INPUT_TABLE, tableName.getNameWithNamespaceInclAsString());
        conf.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = javaSparkContext.newAPIHadoopRDD(conf,
                TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        rdd.cache();
        log.info(">>> read {} from hbase table {}", rdd.count(), tableName.getNameWithNamespaceInclAsString());
        return rdd;
    }
}
