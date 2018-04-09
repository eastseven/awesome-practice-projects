package cn.eastseven.demo.crawler.repository;

import cn.eastseven.demo.crawler.model.SpiderData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author eastseven
 */
public interface SpiderDataRepository extends MongoRepository<SpiderData, String> {
}
