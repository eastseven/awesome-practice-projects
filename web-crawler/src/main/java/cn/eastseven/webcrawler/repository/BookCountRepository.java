package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.BookCount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookCountRepository extends MongoRepository<BookCount, String> {
}
