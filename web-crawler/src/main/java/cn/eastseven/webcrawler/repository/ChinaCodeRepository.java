package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.ChinaCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChinaCodeRepository extends MongoRepository<ChinaCode, String> {
}
