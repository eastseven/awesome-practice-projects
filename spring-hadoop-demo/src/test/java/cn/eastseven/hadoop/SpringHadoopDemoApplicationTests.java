package cn.eastseven.hadoop;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringHadoopDemoApplicationTests {

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Autowired
    HBaseService hbaseService;

    final String tableName = UrlMetadata.TABLE_NAME;

    @Test
    public void testCreateTable() {
        Assert.assertNotNull(hbaseTemplate);

        boolean success = hbaseService.createTable(tableName, new String[]{UrlMetadata.FAMILY_NAME});
        Assert.assertTrue(success);
    }

    @Test
    public void testInsert() {
        String rowKey = UUID.randomUUID().toString();
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "url",        "http://blog.eastseven.cn".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "text",       "Hello HBase".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "origin",     "http://www.eastseven.cn".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "level",      "0".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "createTime", "2017-09-01 18:05:50".getBytes());

        Assert.assertNotNull(rowKey);

        UrlMetadata urlMetadata = hbaseTemplate.get(tableName, rowKey, new RowMapper<UrlMetadata>() {
            @Override
            public UrlMetadata mapRow(Result result, int rowNum) throws Exception {
                String createTime = Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "createTime".getBytes()));
                Date date = DateTime.parse(createTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
                return UrlMetadata.builder()
                        .origin(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "origin".getBytes())))
                        .url(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "url".getBytes())))
                        .text(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "text".getBytes())))
                        .level(Integer.valueOf(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "level".getBytes()))))
                        .createTime(date)
                        .build();
            }
        });
        log.debug("==={}", urlMetadata);
        Assert.assertNotNull(urlMetadata);

    }

    @Test
    public void testFindAll() {
        List<UrlMetadata> list = hbaseTemplate.find(tableName, UrlMetadata.FAMILY_NAME, new RowMapper<UrlMetadata>() {
            @Override
            public UrlMetadata mapRow(Result result, int rowNum) throws Exception {
                String createTime = Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "createTime".getBytes()));
                Date date = DateTime.parse(createTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
                return UrlMetadata.builder()
                        .origin(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "origin".getBytes())))
                        .url(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "url".getBytes())))
                        .text(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "text".getBytes())))
                        .level(Integer.valueOf(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "level".getBytes()))))
                        .createTime(date)
                        .build();
            }
        });

        Assert.assertNotNull(list);
        list.forEach(System.out::println);
    }
}
