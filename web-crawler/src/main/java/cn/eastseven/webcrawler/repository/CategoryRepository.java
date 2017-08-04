package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    Category findByUrl(String url);
}
