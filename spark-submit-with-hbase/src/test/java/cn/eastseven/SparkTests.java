package cn.eastseven;

import cn.eastseven.config.HBaseConfig;
import cn.eastseven.handler.BidNewOriginalTableHandler;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import scala.Tuple2;

import java.io.IOException;
import java.util.List;
import java.util.Set;
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
        final byte[] create_time = Bytes.toBytes("create_time");
        final byte[] category = Bytes.toBytes("category");
        final byte[] key_words = Bytes.toBytes("key_words");
        final byte[] title = Bytes.toBytes("title");

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(new SingleColumnValueFilter(f, original_id, CompareFilter.CompareOp.EQUAL, new SubstringComparator("201712")));
        //filterList.addFilter(new SingleColumnValueFilter(f, category, CompareFilter.CompareOp.EQUAL, new SubstringComparator("其他")));
        Scan scan = new Scan();
        scan.addColumn(f, category);
        scan.addColumn(f, title);
        scan.addColumn(f, key_words);
        scan.addColumn(f, original_id);

        scan.setFilter(filterList);
        Configuration hConf = conf.createNew(TableName.valueOf("bid_news", "bid_news_analysis"), scan);
        Class fClass = TableInputFormat.class;
        Class kClass = ImmutableBytesWritable.class;
        Class vClass = Result.class;
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(hConf, fClass, kClass, vClass);
        long total = rdd.count();

        JavaRDD<Row> resultRDD = rdd.filter(row -> StringUtils.isNotBlank(Bytes.toString(row._2.getValue(f, key_words))))
                .filter(row -> StringUtils.startsWith(Bytes.toString(row._2.getValue(f, original_id)), "201712"))
                .map(row -> {
                    String id = Bytes.toString(row._2.getValue(f, original_id));
                    String titleValue = Bytes.toString(row._2.getValue(f, title));
                    String keywords = Bytes.toString(row._2.getValue(f, key_words));
                    String categoryValue = Bytes.toString(row._2.getValue(f, category));
                    Set<String> set = Sets.newHashSet(keywords.split(" ")).stream()
                            .filter(k -> StringUtils.strip(k).length() > 0).collect(Collectors.toSet());
                    keywords = StringUtils.substringBetween(set.toString(), "[", "]");
                    //return String.join(",", categoryValue, titleValue, keywords);
                    return RowFactory.create(categoryValue, id, titleValue, keywords, Bytes.toString(row._2.getRow()));
                });
        long result = resultRDD.count();
        log.info(">>> total {}, result {}", total, result);

        List<StructField> fieldList = Lists.newArrayList();
        fieldList.add(DataTypes.createStructField("category", DataTypes.StringType, true));
        fieldList.add(DataTypes.createStructField("original_id", DataTypes.StringType, true));
        fieldList.add(DataTypes.createStructField("title", DataTypes.StringType, true));
        fieldList.add(DataTypes.createStructField("keywords", DataTypes.StringType, true));
        fieldList.add(DataTypes.createStructField("rowkey", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fieldList);
        SQLContext sql = SQLContext.getOrCreate(jsc.sc());

        Dataset<Row> df = sql.createDataFrame(resultRDD, schema);
        df.write().csv("target/title_category_" + DateTime.now().toString("yyyy_MM_dd_HH_mm"));
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

    @Test
    public void testCountOriginalTableDailyBySourceCode() throws Exception {
        TableName original = TableName.valueOf("bid_news", "bid_news_original");

        String day = "20171206";
        byte[] f = Bytes.toBytes("f");
        byte[] sourceCode = Bytes.toBytes("sourceCode");
        Scan scan = new Scan();
        scan.addColumn(f, sourceCode);
        scan.setFilter(new PrefixFilter(Bytes.toBytes(day)));

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = loadData(original, scan);
        long total = rdd.count();
        log.info(">>> table {} total {}", original.getNameWithNamespaceInclAsString(), total);

        JavaPairRDD<String, String> result = rdd.mapToPair(row -> new Tuple2<>(Bytes.toString(row._2.getValue(f, sourceCode)), Bytes.toString(row._2.getRow())));
        result.count();

        List<String> data = result.groupByKey().map(row -> row._1 + "=" + Lists.newArrayList(row._2).size()).cache().collect();
        data.forEach(System.out::println);
    }
}
