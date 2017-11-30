package cn.eastseven;

import cn.eastseven.config.HBaseConfig;
import cn.eastseven.handler.BidNewOriginalTableHandler;
import com.google.common.collect.Lists;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Segment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import scala.Tuple2;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SparkTests extends ParentTests {

    @Test
    public void test() {
        Assert.assertNotNull(jsc);
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        List<Integer> rdd = jsc.parallelize(list).flatMap(x -> Lists.newArrayList(x, x + 1).iterator()).collect();
        log.info("\n>>> original = {}\n>>> flat map = {}", list, rdd);
    }

    @Test
    public void testWriteTitle2HTable() throws IOException {
        HBaseConfig conf = new HBaseConfig();
        final byte[] f = Bytes.toBytes("f");
        final byte[] title = Bytes.toBytes("title");
        Scan scan = new Scan();
        scan.addColumn(f, title);
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_analysis"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;

        // (rowKey, title)
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass).cache();
        long total = rdd.count();

        // (title, rowKey)
        JavaPairRDD<String, String> reversalRDD = rdd.mapToPair(result -> new Tuple2(Bytes.toString(result._2.getValue(f, title)), Bytes.toString(result._2.getRow())));
        long reversalCount = reversalRDD.count();

        Assert.assertEquals(total, reversalCount);

        JavaPairRDD<String, Iterable<String>> gbk = reversalRDD.groupByKey();
        long gbkCount = gbk.count();

        JavaPairRDD<String, Iterable<String>> result = gbk.filter(row -> Lists.newArrayList(row._2).size() > 1);
        long resultCount = result.count();
        JavaRDD<String> save = result.keys().cache();
        save.count();

        String path = "target/multi_title_" + DateTime.now().toString("yyyyMMddHHmm");
        save.saveAsTextFile(path);

        log.info(">>> total {}/{}, group by key size = {}, result {}", total, reversalCount, gbkCount, resultCount);

        JavaRDD<String> titles = jsc.textFile(path);
        log.info(">>> read from file {}, size {}", path, titles.count());
    }

    @Test
    public void testOriginalTableFindBySourceCode() throws IOException {
        Assert.assertNotNull(config);
        new BidNewOriginalTableHandler().jobA(config);
    }

    @Test
    public void testAnalysisGroupByCategory() throws Exception {
        Assert.assertNotNull(config);

        HBaseConfig conf = new HBaseConfig();
        final byte[] f = Bytes.toBytes("f");
        final byte[] category = Bytes.toBytes("category");
        final byte[] title = Bytes.toBytes("title");
        final byte[] key_words = Bytes.toBytes("key_words");
        Scan scan = new Scan();
        scan.addColumn(f, category);
        scan.addColumn(f, title);
        scan.addColumn(f, key_words);
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_analysis"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass).cache();
        long total = rdd.count();

        List<String> categories = rdd.map(result -> StringUtils.strip(Bytes.toString(result._2.getValue(f, category)))).distinct().collect();
        log.info(">>> total {}, category {}", total, categories.size());
        categories.forEach(System.out::println);

        JavaPairRDD<String, String> categoryRDD = rdd.mapToPair(result -> {
            String categoryValue = StringUtils.strip(Bytes.toString(result._2.getValue(f, category)));
            String titleValue = StringUtils.strip(Bytes.toString(result._2.getValue(f, title)));
            return new Tuple2(categoryValue, titleValue);
        });

        JavaRDD<String> categoryResultRDD = categoryRDD.groupByKey().map(row -> {
            List<String> list = Lists.newArrayList(row._2);
            int size = list.size();
            String titles = list.stream().limit(5).map(_title -> String.join(",", _title))
                    .collect(Collectors.toList()).toString();
            return String.join("\t", row._1(), String.valueOf(size), titles);
        });

        categoryResultRDD.count();
        categoryResultRDD.collect().forEach(System.out::println);

        categoryResultRDD.saveAsTextFile("target/category_" + DateTime.now().toString("yyyy_MM_dd_HH_mm"));
    }

    @Test
    public void testAnalysisTitleAndKeywordsAndCategory() throws Exception {
        Assert.assertNotNull(config);

        HBaseConfig conf = new HBaseConfig();
        final byte[] f = Bytes.toBytes("f");
        final byte[] original_id = Bytes.toBytes("original_id");
        final byte[] category = Bytes.toBytes("category");
        final byte[] key_words = Bytes.toBytes("key_words");
        final byte[] title = Bytes.toBytes("title");
        Scan scan = new Scan();
        scan.addColumn(f, category);
        scan.addColumn(f, title);
        //scan.addColumn(f, key_words);
        scan.setFilter(new SingleColumnValueFilter(f, original_id, CompareFilter.CompareOp.EQUAL, new SubstringComparator("20171130")));
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_analysis"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass).cache();
        long total = rdd.count();

        JavaRDD<String> resultRDD = rdd.map(row -> {
            String titleValue = Bytes.toString(row._2.getValue(f, title));
            String categoryValue = Bytes.toString(row._2.getValue(f, category));

            String seg = "@";
            if (StringUtils.isNotBlank(titleValue)) {
                Segment segment = new CRFSegment();
                segment.enablePartOfSpeechTagging(true);
                seg = segment.seg(titleValue).stream().map(term -> term.word).distinct().collect(Collectors.toSet()).toString();
            }

            return String.join(" | ", titleValue, seg, categoryValue);
        });
        long result = resultRDD.count();

        log.info(">>> total {}, result {}", total, result);
        resultRDD.saveAsTextFile("target/title_category_" + DateTime.now().toString("yyyy_MM_dd_HH_mm"));
    }

    @Test
    public void testCompanyTable() throws Exception {
        HBaseConfig conf = new HBaseConfig();
        final byte[] f = Bytes.toBytes("f");
        final byte[] name = Bytes.toBytes("name");
        Scan scan = new Scan();
        scan.addColumn(f, name);
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_company"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass).cache();
        long total = rdd.count();

        JavaPairRDD<String, String> companyNameRDD = rdd.mapToPair(row ->
                new Tuple2(StringUtils.strip(Bytes.toString(row._2.getValue(f, name))), Bytes.toString(row._2.getRow())));
        long companyNameCount = companyNameRDD.count();

        JavaPairRDD companyNameGBKRDD = companyNameRDD.groupByKey().cache().filter(row -> Lists.newArrayList(row._2).size() > 1);
        companyNameGBKRDD.saveAsTextFile("target/company_name_" + DateTime.now().toString("yyyy_MM_dd_HH_mm"));

        log.info(">>> bid_news_company total {}, company name count {}", total, companyNameCount);
    }

    @Test
    public void testCountOriginalTableDaily() throws Exception {
        TableName original = TableName.valueOf("bid_news", "bid_news_original");

        DateTime start = new DateTime("2017-11-01");
        DateTime end = new DateTime("2017-11-30");
        Duration duration = new Duration(start, end);
        for (int day = 0; day < duration.getStandardDays(); day++) {
            String date = start.plusDays(day).toString("yyyyMMdd");
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
            filterList.addFilter(new FirstKeyOnlyFilter());
            filterList.addFilter(new PrefixFilter(Bytes.toBytes(date)));
            Scan scan = new Scan();
            scan.setFilter(filterList);
            JavaPairRDD<ImmutableBytesWritable, Result> rdd = loadData(original, scan);
            long total = rdd.count();
            log.info(">>> {} table {} total {}", date, original.getNameWithNamespaceInclAsString(), total);
        }

    }
}
