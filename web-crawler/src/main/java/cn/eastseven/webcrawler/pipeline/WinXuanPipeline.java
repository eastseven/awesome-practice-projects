package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.WinXuan;
import cn.eastseven.webcrawler.repository.WinXuanRepository;
import lombok.extern.slf4j.Slf4j;
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
    WinXuanRepository winXuanRepository;

    @Override
    public void process(WinXuan winXuan, Task task) {
        winXuanRepository.save(winXuan);
    }
}
