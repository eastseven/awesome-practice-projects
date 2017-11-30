package cn.eastseven;

import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.huaban.analysis.jieba.JiebaSegmenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TextTests extends ParentTests {

    private TableName original = TableName.valueOf("bid_news", "bid_news_original");

    @Test
    public void test() throws Exception {
        final byte[] f = Bytes.toBytes("f");
        final byte[] title = Bytes.toBytes("title");
        final byte[] name = Bytes.toBytes("name");
        Scan scan = new Scan();
        scan.addColumn(f, title);
        scan.setMaxResultSize(100L);
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = loadData(original, scan).cache();

        long count = rdd.count();

        JavaRDD<String> titles = rdd.map(row -> Bytes.toString(row._2.getValue(f, title))).distinct();
        long tCount = titles.count();
        log.info(">>> {} total {}, distinct {}", original.getNameWithNamespaceInclAsString(), count, tCount);
        titles.saveAsTextFile("data/titles");
    }

    @Test
    public void testParticiple() throws IOException {
        File file = Paths.get("data", "titles").toFile();
        Assert.assertTrue(file.exists());

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("part-");
            }
        });

        List<String> titles = Files.readAllLines(Paths.get("", files[0].getAbsolutePath()));

        Segment segment = new CRFSegment();
        segment.enablePartOfSpeechTagging(true);

        String[] testCase = titles.toArray(new String[titles.size()]);
        for (String sentence : testCase) {
            log.info("{}\t{}", sentence, segment.seg(sentence).stream().map(term -> term.word).filter(v -> v.length() > 1).distinct().collect(Collectors.toSet()));
        }
    }

    @Test
    public void testTitle() {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment segment = new CRFSegment();
        segment.enablePartOfSpeechTagging(true);

        String title = "珠海市斗门区中小企业局台式计算机网上询价合同";

        log.info(">>> {}", title);
        log.info(">>> CRFSegment {}", segment.seg(title).stream().map(term -> term.word).distinct().collect(Collectors.toSet()));

        //https://github.com/hankcs/HanLP
        //https://github.com/huaban/jieba-analysis

        log.info(">>> {}", segmenter.sentenceProcess(title));
        log.info(">>> {}", segmenter.process(title, JiebaSegmenter.SegMode.INDEX).stream().map(segToken -> segToken.word).distinct().collect(Collectors.toSet()));
        log.info(">>> {}", segmenter.process(title, JiebaSegmenter.SegMode.SEARCH).stream().map(segToken -> segToken.word).distinct().collect(Collectors.toSet()));
    }
}
