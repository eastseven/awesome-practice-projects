package cn.eastseven.handler;

import cn.eastseven.config.HBaseConfig;
import cn.eastseven.config.SparkConfig;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.joda.time.DateTime;

import java.io.IOException;

/**
 * @author eastseven
 */
public class ProjectTitleHandler {

    public static void main(String[] args) throws IOException {
        String date = DateTime.now().toString("yyyyMMdd_HHmmss");
        SparkConfig config = new SparkConfig();
        HBaseConfig hbaseConfig = new HBaseConfig();
        JavaSparkContext jsc = config.getJavaSparkContext();

        final byte[] f = "f".getBytes();
        final byte[] title = "title".getBytes();
        final byte[] sameBidId = "same_bid_id".getBytes();

        Scan projectScan = hbaseConfig.newScan();
        projectScan.addColumn(f, title);
        projectScan.addColumn(f, sameBidId);
        TableName projectTable = TableName.valueOf("bid_news", "bid_news_project");
        JavaPairRDD<ImmutableBytesWritable, Result> projectRDD = jsc.newAPIHadoopRDD(hbaseConfig.createNew(projectTable),
                TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        //全部 项目表ID
        long count = projectRDD.count();
        JavaRDD<String> projectIdRDD = projectRDD.map(row -> Bytes.toString(row._2.getValue(f, sameBidId)));
        projectIdRDD.saveAsTextFile("/data/bid_news_project_ids_" + date);
        long distinct = projectIdRDD.count();
        System.out.println(">>>\tproject count " + count + ", distinct " + distinct);

        //全部 公告表中的项目ID
        TableName analysisTable = TableName.valueOf("bid_news", "bid_news_analysis");
        Scan scan = hbaseConfig.newScan();
        scan.addColumn(f, title);
        scan.addColumn(f, sameBidId);
        JavaPairRDD<ImmutableBytesWritable, Result> biddingRDD = jsc.newAPIHadoopRDD(hbaseConfig.createNew(analysisTable, scan),
                TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
        long biddingCount = biddingRDD.count();
        JavaRDD<String> projectIdInBiddingRDD = biddingRDD.map(row -> Bytes.toString(row._2.getValue(f, sameBidId))).cache();
        projectIdInBiddingRDD.saveAsTextFile("/data/bid_news_analysis_project_ids_" + date);
        System.out.println(">>>\tbidding count " + biddingCount + ", same_bid_id in bid_news_analysis " + projectIdInBiddingRDD.count());

        // 交集
        JavaRDD<String> resultRDD1 = projectIdInBiddingRDD.intersection(projectIdRDD);
        System.out.println(">>>\tvalid same_bid_id size " + resultRDD1.count());
        resultRDD1.saveAsTextFile("/data/valid_same_bid_id_1_" + date);

        // 需要删除的same_bid_id集合
        JavaRDD<String> needDeleteProjectIdsRDD1 = projectIdRDD.subtract(resultRDD1);
        System.out.println(">>>\tneed delete project ids " + needDeleteProjectIdsRDD1.count());
        needDeleteProjectIdsRDD1.saveAsTextFile("/data/invalid_same_bid_id_" + date);

        jsc.close();
    }

}
