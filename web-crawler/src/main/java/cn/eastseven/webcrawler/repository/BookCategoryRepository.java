package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.BookCategory;
import cn.eastseven.webcrawler.model.BookOrigin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookCategoryRepository extends MongoRepository<BookCategory, String> {

    List<BookCategory> findByOrigin(BookOrigin origin);

    Page<BookCategory> findByOrigin(BookOrigin origin, Pageable pageable);
}
