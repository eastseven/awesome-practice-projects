package cn.eastseven.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSentinelWithSpringApplicationTests {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void contextLoads() {
        Assert.assertNotNull(redisTemplate);
        String key = "redisTestKey";
        String value = "I am test value";

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        //数据插入测试：
        opsForValue.set(key, value);
        String valueFromRedis = opsForValue.get(key);
        log.info("redis value after set: {}", valueFromRedis);
        assertThat(valueFromRedis, is(value));

        //数据删除测试：
        redisTemplate.delete(key);
        valueFromRedis = opsForValue.get(key);
        log.info("redis value after delete: {}", valueFromRedis);
        assertThat(valueFromRedis, equalTo(null));
    }

}
