package cn.eastseven.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @author eastseven
 */
public class SparkConfig {

    private JavaSparkContext javaSparkContext;

    public JavaSparkContext getJavaSparkContext() {
        return javaSparkContext;
    }

    public SparkConfig() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("spark-submit-with-hbase");
        sparkConf.setMaster("spark://s97:7077");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        javaSparkContext = new JavaSparkContext(sparkConf);
    }

    public SparkConfig(String master) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("spark-submit-with-hbase");
        sparkConf.setMaster(master);
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        javaSparkContext = new JavaSparkContext(sparkConf);
    }
}
