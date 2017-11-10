package cn.eastseven.spark;

import cn.eastseven.spark.service.SparkHBaseService;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SparkStatisticsTests {

    final String path = "spark-warehouse/bidding_titles";

    @Autowired
    SparkHBaseService service;

    @Test
    public void test() {
        assertNotNull(path);

        JavaRDD<String> rdd = service.getJavaSparkContext().textFile(path);
        assertNotNull(rdd);

        rdd.cache();

        JavaRDD<String> sentenceRdd = rdd.map(f -> {
            String line = StringUtils.replacePattern(Bytes.toString(f.getBytes()), "[\\pP‘’“”]", "");
            String words = NLPTokenizer.segment(line).stream().map(term -> term.word).collect(Collectors.joining("-"));

            return words;
        });

        sentenceRdd.foreach(f->log.debug("{}", Bytes.toString(f.getBytes())));

        /*SparkSession spark = SparkSession.builder()
                .master("local")
                .appName("SparkHBaseService")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .getOrCreate();
        Dataset<String> dataset = spark.createDataset(sentenceRdd.rdd(), Encoders.STRING());
        HashingTF hashingTF = new HashingTF();
        Dataset<Row> rowDataset = hashingTF.transform(dataset);
        log.debug(">>> {}", rowDataset.count());*/
    }

}
