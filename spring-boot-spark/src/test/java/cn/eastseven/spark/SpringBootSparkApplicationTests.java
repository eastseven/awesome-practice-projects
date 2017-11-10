package cn.eastseven.spark;

import cn.eastseven.spark.config.HBaseConfig;
import cn.eastseven.spark.config.KeyWordsProperties;
import cn.eastseven.spark.dao.BiddingArticle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hankcs.hanlp.HanLP;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.WriteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootSparkApplicationTests {

    @Value("${spring.data.mongodb.uri}")
    String uri;

    @Autowired
    HBaseConfig hbaseConfig;

    @Autowired
    KeyWordsProperties keyWordsProperties;

    private Map<String, String[]> keyWordsMap = Maps.newHashMap();

    private JavaSparkContext jsc;

    private Configuration configuration;

    @Before
    public void setup() {
        Assert.assertNotNull(keyWordsProperties);
        keyWordsMap = keyWordsProperties.getKeyWordsMap();
        Assert.assertFalse(keyWordsMap.isEmpty());

        SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("MongoSparkConnectorIntro")
                .config("spark.mongodb.input.uri", uri + ".bidding_article")
                .config("spark.mongodb.output.uri", uri + ".bidding_article")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();
        Assert.assertNotNull(spark);

        // Create a JavaSparkContext using the SparkSession's SparkContext object
        jsc = new JavaSparkContext(spark.sparkContext());

        Assert.assertNotNull(hbaseConfig.getTableOriginal());

        configuration = hbaseConfig.get();
        Assert.assertNotNull(configuration);
    }

    @After
    public void teardown() {
        jsc.close();
    }

    @Test
    public void titleWordsStatistics() throws Exception {
        Scan scan = new Scan();
        scan.addFamily("f".getBytes());
        scan.addColumn("f".getBytes(), "sourceCode".getBytes("utf-8"));
        scan.addColumn("f".getBytes(), "title".getBytes("utf-8"));
        scan.addColumn("f".getBytes(), "type".getBytes("utf-8"));
        scan.setFilter(new SingleColumnValueFilter("f".getBytes(), "type".getBytes(), CompareFilter.CompareOp.EQUAL, new RegexStringComparator(".+招标+.")));

        Assert.assertNotNull(scan);

        configuration.set(TableInputFormat.INPUT_TABLE, hbaseConfig.getTableOriginal().getNameWithNamespaceInclAsString());
        configuration.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(configuration,
                TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
        rdd.cache();
        log.info(">>> all count {}", rdd.count());

        JavaRDD<Document> documentJavaRDD = rdd.map(f -> {
            String rowKey = Bytes.toString(f._1.get());
            String sourceCode = Bytes.toString(f._2.getValue("f".getBytes(), "sourceCode".getBytes("utf8")));
            String title = Bytes.toString(f._2.getValue("f".getBytes(), "title".getBytes("utf8")));
            String type = Bytes.toString(f._2.getValue("f".getBytes(), "type".getBytes("utf8")));

            log.debug(">>> {}-{}\t{}\t{}", sourceCode, type, title, HanLP.extractKeyword(title, 5).toString());
            Document document = new Document();
            return document;
        });

        log.debug(">>> documents count {}", documentJavaRDD.count());
    }

    @Test
    public void contextLoads() throws Exception {
        Assert.assertNotNull(keyWordsProperties);

        Scan scan = new Scan();
        scan.addFamily("f".getBytes());
        scan.addColumn("f".getBytes(), "sourceCode".getBytes("utf-8"));
        scan.addColumn("f".getBytes(), "title".getBytes("utf-8"));
        scan.addColumn("f".getBytes(), "formatContent".getBytes("utf-8"));
        scan.addColumn("f".getBytes(), "url".getBytes("utf-8"));

        configuration.set(TableInputFormat.INPUT_TABLE, hbaseConfig.getNamespace() + ':' + hbaseConfig.getTableOriginal());
        configuration.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = jsc.newAPIHadoopRDD(configuration,
                TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

        rdd.cache();

        long count = rdd.count();
        log.info(">>> all count {}", count);

        Map<String, String[]> map = keyWordsProperties.getKeyWordsMap();
        JavaRDD<Document> biddingArticleJavaRDD = rdd.map(f -> {
            JiebaSegmenter segmenter = new JiebaSegmenter();
            String rowKey = Bytes.toString(f._1.get());
            String title = Bytes.toString(f._2.getValue("f".getBytes(), "title".getBytes("utf8")));
            String sourceCode = Bytes.toString(f._2.getValue("f".getBytes(), "sourceCode".getBytes("utf8")));
            String url = Bytes.toString(f._2.getValue("f".getBytes(), "url".getBytes("utf8")));

            String titleWords = segmenter.sentenceProcess(title).stream().collect(Collectors.joining(" "));

            Map<Integer, String> scoreMap = Maps.newHashMap();
            for (String key : map.keySet()) {
                String[] words = map.get(key);
                Integer score = (int) Lists.newArrayList(words).stream().filter(word -> titleWords.contains(word)).count();
                if (!scoreMap.containsKey(score)) {
                    scoreMap.put(score, key);
                } else {
                    String value = scoreMap.get(score) + ',' + key;
                    scoreMap.put(score, value);
                }
            }

            Integer scoreKey = scoreMap.keySet().stream().max((a, b) -> Integer.compare(a, b)).get();
            String category = scoreKey > 0 ? scoreMap.get(scoreKey) : "其他";

            /*if (StringUtils.equals(category, "其他")) {
                scoreMap.clear();

                String formatContent = Bytes.toString(f._2.getValue("f".getBytes(), "formatContent".getBytes("utf8")));
                String text = Jsoup.clean(formatContent, Whitelist.none());
                String textWordsArray = segmenter.sentenceProcess(text).stream()
                        .filter(word -> StringUtils.isNotBlank(word) && word.length() > 1)
                        .map(word -> StringUtils.trim(word))
                        .collect(Collectors.joining(","));

                for (String key : map.keySet()) {
                    String[] words = map.get(key);
                    Integer score = (int) Arrays.stream(words).filter(word -> textWordsArray.contains(word)).count();

                    if (!scoreMap.containsKey(score)) {
                        scoreMap.put(score, key);
                    } else {
                        scoreMap.put(score, String.join(",", scoreMap.get(score), key));
                    }
                }

                Integer key = scoreMap.keySet().stream().max((a, b) -> Integer.compare(a, b)).get();
                category = key > 0 ? scoreMap.get(key) : "其他";
            }*/

            BiddingArticle article = new BiddingArticle();
            article.setRowKey(rowKey);
            article.setUrl(url);
            article.setCode(sourceCode);
            article.setIndustry(category);
            article.setTitle(title);
            //log.debug(">>>\t{}\t[{}]\t\t\t{}\t{}", sourceCode, category, title, url);

            Document document = new Document();
            document.putAll(BeanUtils.describe(article));
            return document;
        });

        log.debug("biddingArticleJavaPairRDD = {}", biddingArticleJavaRDD.count());

        // Create a custom WriteConfig
        Map<String, String> writeOverrides = Maps.newHashMap();
        writeOverrides.put("collection", "bidding_article");
        writeOverrides.put("writeConcern.w", "majority");
        WriteConfig writeConfig = WriteConfig.create(jsc).withOptions(writeOverrides);

        MongoSpark.save(biddingArticleJavaRDD, writeConfig);
    }

    @Test
    public void testJieba() {
        String text = "广西新宇建设项目管理有限公司那坡县2017年县人大代表建议办理专项资金计划项目建设：城厢镇中强村村级路至各更屯屯级道路硬化、城厢镇者兰村级路至者兰下屯屯级道路硬化、城厢镇龙华村级路至龙华村弄香屯屯级道路硬化等16条道路硬化工程监理";

        Assert.assertNotNull(text);

        JiebaSegmenter segmenter = new JiebaSegmenter();
        segmenter.sentenceProcess(text).forEach(System.out::println);
    }
}
