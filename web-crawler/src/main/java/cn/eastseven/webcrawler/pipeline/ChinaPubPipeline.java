package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.Book;
import cn.eastseven.webcrawler.model.BookOrigin;
import cn.eastseven.webcrawler.model.ChinaPub;
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
public class ChinaPubPipeline implements PageModelPipeline<ChinaPub> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public void process(ChinaPub chinaPub, Task task) {
        if (bookRepository.findByUrl(chinaPub.getUrl()) == null) {
            Book book = Book.builder().origin(BookOrigin.CHINA_PUB).build();
            BeanUtils.copyProperties(chinaPub, book);
            bookRepository.save(book);
            log.debug("{}", book);
        }
    }
}
