package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.DangDang;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DangDangRepository extends MongoRepository<DangDang, String> {
}
