package cn.eastseven.demo.crawler.repository;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author eastseven
 */
public interface SpiderConfigRepository extends MongoRepository<SpiderConfig, String> {
}
