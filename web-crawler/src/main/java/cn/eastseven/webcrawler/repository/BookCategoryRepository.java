package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.BookCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookCategoryRepository extends MongoRepository<BookCategory, String> {
}
