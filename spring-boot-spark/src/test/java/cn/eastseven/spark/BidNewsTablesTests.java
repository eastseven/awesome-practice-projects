package cn.eastseven.spark;

import cn.eastseven.spark.config.HBaseConfig;
import cn.eastseven.spark.model.SourceCode;
import cn.eastseven.spark.service.SparkHBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.EQUAL;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BidNewsTablesTests {

    @Autowired
    SparkHBaseService service;

    @Autowired
    HBaseConfig config;

    @Test
    public void test() throws IOException {
        assertTrue(CollectionUtils.isNotEmpty(config.getTableNames()));

        for (TableName tableName : config.getTableNames()) {
            long total = service.getHBaseResultJavaPairRDD(tableName).count();
            log.info(">>> {} = {}", tableName.getNameWithNamespaceInclAsString(), total);
        }
    }

    @Test
    public void testTableOriginal() throws Exception {
        assertNotNull(config.getTableOriginal());

        final byte[] f = Bytes.toBytes("f");
        final byte[] url = Bytes.toBytes("url");
        final byte[] type = Bytes.toBytes("type");
        final byte[] title = Bytes.toBytes("title");
        final byte[] sourceCode = Bytes.toBytes("sourceCode");
        final byte[] formatContent = Bytes.toBytes("formatContent");

        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, url);
        scan.addColumn(f, sourceCode);
        scan.addColumn(f, title);
        scan.addColumn(f, type);
        scan.setFilter(new SingleColumnValueFilter(f, type, EQUAL, new SubstringComparator("交易公告")));
        //scan.setFilter(new SingleColumnValueFilter(f, type, EQUAL, new SubstringComparator("合同公告")));

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> {} {}", config.getTableOriginal(), rdd.count());

        Assert.assertTrue(rdd.count() > 0L);

        rdd.foreach(result -> {
            String rowKey = Bytes.toString(result._1.get());
            String typeValue = Bytes.toString(result._2.getValue(f, type));
            String titleOriginalValue = Bytes.toString(result._2.getValue(f, title));
            String sourceCodeValue = Bytes.toString(result._2.getValue(f, sourceCode));
            String urlValue = Bytes.toString(result._2.getValue(f, url));

            log.debug(">>> {}, {}, {}, {}, {}", rowKey, sourceCodeValue, typeValue, titleOriginalValue, urlValue);
        });

        //JavaRDD<String> titleTextRDD = rdd.map(result -> StringUtils.strip(Bytes.toString(result._2.getValue(f, title))));
        //final String path = "spark-warehouse/bidding_titles";
        //Files.deleteIfExists(Paths.get("spark-warehouse", "bidding_titles"));
        //titleTextRDD.saveAsTextFile(path);
    }

    @Test
    public void testType() throws Exception {
        final byte[] f = Bytes.toBytes("f");
        final byte[] url = Bytes.toBytes("url");
        final byte[] type = Bytes.toBytes("type");
        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, type);
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> {} {}", config.getTableOriginal(), rdd.count());

        JavaRDD<String> typeRdd = rdd.map(row -> Bytes.toString(row._2.getValue(f, type))).distinct();
        log.debug(">>> type rdd {}", typeRdd.count());

        typeRdd.distinct().foreach(row -> log.debug(">>> distinct type {}", row));

        assertNotNull(typeRdd);
    }

    @Test
    public void testSourceCode() throws Exception {
        final byte[] f = Bytes.toBytes("f");

        final byte[] url = Bytes.toBytes("url");
        final byte[] type = Bytes.toBytes("type");
        final byte[] title = Bytes.toBytes("title");
        final byte[] projectName = Bytes.toBytes("project_name");
        final byte[] sourceCode = Bytes.toBytes("sourceCode");
        final byte[] date = Bytes.toBytes("date");

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        //filterList.addFilter(new QualifierFilter(EQUAL, new BinaryComparator(sourceCode)));
        filterList.addFilter(new SingleColumnValueFilter(f, sourceCode, EQUAL, new SubstringComparator(SourceCode.CCGPHN.toString())));

        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, url);
        scan.addColumn(f, type);
        scan.addColumn(f, title);
        scan.addColumn(f, projectName);
        scan.addColumn(f, sourceCode);
        scan.addColumn(f, date);
        //scan.setFilter(filterList);
        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> count table {} {}", config.getTableOriginal(), rdd.count());

        Assert.assertTrue(rdd.count() > 0L);

        rdd.foreach(result -> {
            String urlValue = Bytes.toString(result._2.getValue(f, url));
            String sourceCodeValue = Bytes.toString(result._2.getValue(f, sourceCode));
            String typeValue = Bytes.toString(result._2.getValue(f, type));
            String titleValue = Bytes.toString(result._2.getValue(f, title));
            String projectNameValue = Bytes.toString(result._2.getValue(f, projectName));
            String dateValue = Bytes.toString(result._2.getValue(f, date));

            if (StringUtils.equalsIgnoreCase(sourceCodeValue, SourceCode.CCGPHN.toString())) {
                log.debug("date={}\tsource={}\ttype={}\tproject={}\tarticle={}", dateValue, sourceCodeValue, typeValue, projectNameValue, titleValue);
            }
        });
    }

    @Test
    public void testGroupByProjectName() throws Exception {
        final byte[] f = Bytes.toBytes("f");
        final byte[] url = Bytes.toBytes("url");
        final byte[] type = Bytes.toBytes("type");
        final byte[] title = Bytes.toBytes("title");
        final byte[] projectName = Bytes.toBytes("project_name");

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(new QualifierFilter(EQUAL, new BinaryComparator(url)));
        filterList.addFilter(new QualifierFilter(EQUAL, new BinaryComparator(type)));
        filterList.addFilter(new QualifierFilter(EQUAL, new BinaryComparator(title)));
        filterList.addFilter(new QualifierFilter(EQUAL, new BinaryComparator(projectName)));
        //filterList.addFilter(new SingleColumnValueFilter(f, projectName, NOT_EQUAL, new NullComparator()));

        Scan scan = new Scan();
        scan.addFamily(f);
        scan.addColumn(f, url);
        scan.addColumn(f, type);
        scan.addColumn(f, title);
        scan.addColumn(f, projectName);
        scan.setFilter(filterList);

        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableOriginal(), scan);
        log.info(">>> count table {} {}", config.getTableOriginal(), rdd.count());

        Assert.assertTrue(rdd.count() > 0L);

        rdd.foreach(result -> {
            String urlValue = Bytes.toString(result._2.getValue(f, url));
            String typeValue = Bytes.toString(result._2.getValue(f, type));
            String titleValue = Bytes.toString(result._2.getValue(f, title));
            String projectNameValue = Bytes.toString(result._2.getValue(f, projectName));

            if (StringUtils.isNotBlank(projectNameValue)) {
                log.debug("类型={}\t项目={}\t公告={}", typeValue, projectNameValue, titleValue);
            }
        });
    }

    /**
     * mvn -Dtest=BidNewsTablesTests#testAnalysisAndProjectAndCompany test -Paliyun -Dspring.profiles.active=prod
     *
     * @throws Exception
     */
    @Test
    public void testAnalysisAndProjectAndCompany() throws Exception {
        Scan scan = new Scan();


        JavaPairRDD<ImmutableBytesWritable, Result> rdd = service.getHBaseResultJavaPairRDD(config.getTableAnalysis(), scan);
        log.debug(">>> count {} {}", config.getTableAnalysis().getNameWithNamespaceInclAsString(), rdd.count());

    }
}
