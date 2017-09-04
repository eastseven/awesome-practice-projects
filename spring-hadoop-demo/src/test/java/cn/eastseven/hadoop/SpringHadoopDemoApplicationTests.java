package cn.eastseven.hadoop;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.repository.UrlMetadataRowMapper;
import cn.eastseven.hadoop.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "url", "http://blog.eastseven.cn".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "text", "Hello HBase".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "origin", "http://www.eastseven.cn".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "level", "0".getBytes());
        hbaseTemplate.put(tableName, rowKey, UrlMetadata.FAMILY_NAME, "createTime", "2017-09-01 18:05:50".getBytes());

        Assert.assertNotNull(rowKey);

        UrlMetadata urlMetadata = hbaseTemplate.get(tableName, rowKey, new UrlMetadataRowMapper());
        log.debug("==={}", urlMetadata);
        Assert.assertNotNull(urlMetadata);

    }

    @Test
    public void testFindAll() {
        List<UrlMetadata> list = hbaseTemplate.find(tableName, UrlMetadata.FAMILY_NAME, new UrlMetadataRowMapper());

        Assert.assertNotNull(list);
        list.forEach(System.out::println);
    }
}
