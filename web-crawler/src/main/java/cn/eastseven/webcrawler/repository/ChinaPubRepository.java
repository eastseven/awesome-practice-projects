package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.ChinaPub;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChinaPubRepository extends MongoRepository<ChinaPub, String> {
}
