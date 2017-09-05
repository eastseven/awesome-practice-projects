package cn.eastseven.hadoop;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.repository.UrlMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UrlMetadataRepositoryTests {

    @Autowired
    UrlMetadataRepository urlMetadataRepository;

    @Test
    public void test() {
        Assert.assertNotNull(urlMetadataRepository);
        List<UrlMetadata> all = Lists.newArrayList(urlMetadataRepository.findAll());
        Assert.assertNotNull(all);

        all.forEach(System.out::println);
    }

    @Test
    public void testReflect() {
        Field[] fields = UrlMetadata.class.getDeclaredFields();
        Assert.assertNotNull(fields);

        for (Field field : fields) {
            log.debug("=== {} === {}", field.getName(), Modifier.isPrivate(field.getModifiers()));
        }

    }

    @Test
    public void testSave() {
        UrlMetadata urlMetadata = UrlMetadata.builder()
                .createTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"))
                .origin("http://www.baidu.com")
                .level(0)
                .text("百度地图")
                .url("http://map.baidu.com")
                .build();
        UrlMetadata saved = urlMetadataRepository.save(urlMetadata);
        Assert.assertNotNull(saved.getId());

        UrlMetadata inDb = urlMetadataRepository.findOne(saved.getId());
        Assert.assertNotNull(inDb);
        log.debug("{}", inDb);

    }
}