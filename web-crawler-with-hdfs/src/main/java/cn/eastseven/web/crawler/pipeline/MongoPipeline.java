package cn.eastseven.web.crawler.pipeline;

import cn.eastseven.web.crawler.model.Bidding;
import cn.eastseven.web.crawler.model.BiddingContent;
import cn.eastseven.web.crawler.repository.BiddingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Slf4j
@Component
public class MongoPipeline implements Pipeline {

    @Autowired
    BiddingRepository biddingRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.debug(" ==== save to mongo ==== {}", task.getUUID());

        Bidding bidding = resultItems.get(Bidding.class.getName());
        if (bidding == null) {
            resultItems.setSkip(true);
            return;
        }
        List<Bidding> biddingList = biddingRepository.findByUrl(bidding.getUrl());
        if (CollectionUtils.isEmpty(biddingList)) {
            String html = bidding.getHtml();
            bidding.setHtml(null);
            biddingRepository.save(bidding);

            BiddingContent content = new BiddingContent(bidding.getId(), html);
            resultItems.put(BiddingContent.class.getName(), content);
        }
    }
}
