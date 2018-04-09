package cn.eastseven.demo.crawler.pipeline;

import cn.eastseven.demo.crawler.model.SpiderData;
import cn.eastseven.demo.crawler.repository.SpiderDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author eastseven
 */
@Slf4j
@Component
public class SpiderDataPipeline implements Pipeline {

    @Autowired private SpiderDataRepository repository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        SpiderData data = resultItems.get(SpiderData.class.getName());
        if (data != null) {
            repository.save(data);
        }
    }
}
