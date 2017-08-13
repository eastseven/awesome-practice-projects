package cn.eastseven.kafka;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaLearningApplicationTests {

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(kafkaTemplate);
        ListenableFuture<SendResult> future = kafkaTemplate.send("test", "test from spring boot kafka");
        future.addCallback(success -> log.debug("success {}, {}", success.getProducerRecord(), success.getRecordMetadata()),
                fail -> log.debug("fail {}", fail.getMessage()));
        log.debug("{}", future);
    }

}
