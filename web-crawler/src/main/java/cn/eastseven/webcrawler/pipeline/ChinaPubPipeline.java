package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.repository.ChinaPubRepository;
import lombok.extern.slf4j.Slf4j;
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
    ChinaPubRepository chinaPubRepository;

    @Override
    public void process(ChinaPub chinaPub, Task task) {
        chinaPubRepository.save(chinaPub);
    }
}
