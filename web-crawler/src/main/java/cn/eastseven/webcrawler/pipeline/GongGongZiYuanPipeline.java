package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.GongGongZiYuanStatistics;
import cn.eastseven.webcrawler.repository.GongGongZiYuanStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Slf4j
@Component
public class GongGongZiYuanPipeline implements Pipeline {

    @Autowired
    GongGongZiYuanStatisticsRepository repository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.debug("{}", resultItems);
        GongGongZiYuanStatistics data = resultItems.get(GongGongZiYuanStatistics.class.getSimpleName());
        repository.save(data);
    }
}
