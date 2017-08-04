package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByUrl(String url);
}
