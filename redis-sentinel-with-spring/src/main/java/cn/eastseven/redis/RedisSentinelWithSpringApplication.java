package cn.eastseven.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Slf4j
@SpringBootApplication
public class RedisSentinelWithSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RedisSentinelWithSpringApplication.class, args);
	}

	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Override
	public void run(String... args) throws Exception {
		String key = "redisTestKey";
		String value = "我是测试数据";

		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		//数据插入测试：
		opsForValue.set(key, value);
		String valueFromRedis = opsForValue.get(key);
		log.info("redis value after set: {}", valueFromRedis);
		//数据删除测试：
		redisTemplate.delete(key);
		valueFromRedis = opsForValue.get(key);
		log.info("redis value after delete: {}", valueFromRedis);
	}
}
