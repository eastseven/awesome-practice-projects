package cn.eastseven.webcrawler.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@Slf4j
@Configuration
public class RedisConfig {

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    @Bean
    RedisScheduler redisScheduler() {
        JedisPoolConfig config = jedisConnectionFactory.getPoolConfig();
        log.debug("{}", config);
        log.debug("{}", jedisConnectionFactory.getPassword());
        JedisPool jedisPool = new JedisPool(config,
                jedisConnectionFactory.getHostName(),
                jedisConnectionFactory.getPort(),
                jedisConnectionFactory.getTimeout(),
                jedisConnectionFactory.getPassword(),
                jedisConnectionFactory.getDatabase());

        RedisScheduler redisScheduler = new RedisScheduler(jedisPool);
        return redisScheduler;
    }
}
