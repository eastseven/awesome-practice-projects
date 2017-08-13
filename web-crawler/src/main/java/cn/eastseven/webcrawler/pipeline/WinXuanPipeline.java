package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.Book;
import cn.eastseven.webcrawler.model.BookOrigin;
import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

@Slf4j
@Component
@Transactional
public class WinXuanPipeline implements PageModelPipeline<WinXuan> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public void process(WinXuan winXuan, Task task) {
        if (bookRepository.findByUrl(winXuan.getUrl()) == null) {
            Book book = Book.builder().origin(BookOrigin.WIN_XUAN).build();
            BeanUtils.copyProperties(winXuan, book);
            bookRepository.save(book);
            log.debug("{}", book);
        }
    }
}
