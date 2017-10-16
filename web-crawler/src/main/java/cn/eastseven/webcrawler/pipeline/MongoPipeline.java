package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.model.DangDang;
import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.repository.BookCategoryRepository;
import cn.eastseven.webcrawler.repository.ChinaPubRepository;
import cn.eastseven.webcrawler.repository.DangDangRepository;
import cn.eastseven.webcrawler.repository.WinXuanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

@Slf4j
@Component
public class MongoPipeline implements PageModelPipeline {

    @Autowired
    DangDangRepository dangDangRepository;

    @Autowired
    ChinaPubRepository chinaPubRepository;

    @Autowired
    WinXuanRepository winXuanRepository;

    @Autowired
    BookCategoryRepository categoryRepository;

    @Override
    public void process(Object data, Task task) {

        if (data instanceof DangDang) {
            save((DangDang) data);
        }

        if (data instanceof ChinaPub) {
            save((ChinaPub) data);
        }

        if (data instanceof WinXuan) {
            save((WinXuan) data);
        }
    }

    void save(DangDang data) {
        dangDangRepository.save(data);
        if (!data.getCategories().isEmpty()) categoryRepository.save(data.getCategories());
        log.info("当当 {}, {}, {}", data.getName(), data.getPrice(), data.getUrl());
    }

    void save(ChinaPub data) {
        chinaPubRepository.save(data);
        if (!data.getCategories().isEmpty()) categoryRepository.save(data.getCategories());
        log.info("互动 {}, {}, {}", data.getName(), data.getPrice(), data.getUrl());
    }

    void save(WinXuan data) {
        winXuanRepository.save(data);
        if (!data.getCategories().isEmpty()) categoryRepository.save(data.getCategories());
        log.info("文轩 {}, {}, {}", data.getName(), data.getPrice(), data.getUrl());
    }
}
