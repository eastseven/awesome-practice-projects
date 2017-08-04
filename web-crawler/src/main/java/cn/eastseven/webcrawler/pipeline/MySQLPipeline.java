package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.Book;
import cn.eastseven.webcrawler.model.Category;
import cn.eastseven.webcrawler.repository.BookRepository;
import cn.eastseven.webcrawler.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Transactional
public class MySQLPipeline implements Pipeline {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> data = resultItems.getAll();

        for (String key : data.keySet()) {
            switch (key) {
                case "categoryList":
                    saveCategoryList(resultItems.get(key));
                    break;
                case "bookList":
                    saveBookList(resultItems.get(key));
                    break;
            }
        }
    }

    void saveCategoryList(List<Category> categoryList) {
        categoryList.forEach(category -> {
            if (categoryRepository.findByUrl(category.getUrl()) == null) {
                categoryRepository.save(category);
                log.debug("{}", category);
            }
        });
    }

    void saveBookList(List<Book> bookList) {
        bookList.forEach(book -> {
            if (bookRepository.findByUrl(book.getUrl()) == null) {
                bookRepository.save(book);
                log.debug("{}", book);
            }
        });
    }
}
