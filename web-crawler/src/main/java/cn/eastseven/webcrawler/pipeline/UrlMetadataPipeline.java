package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.UrlMetadata;
import cn.eastseven.webcrawler.repository.UrlMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Slf4j
@Component
@Transactional
public class UrlMetadataPipeline implements Pipeline {

    @Autowired
    UrlMetadataRepository dao;

    @Override
    public void process(ResultItems resultItems, Task task) {

        List<UrlMetadata> urlMetadataList = resultItems.get("urlMetadataList");
        if (urlMetadataList != null) {
            List<UrlMetadata> list = Lists.newArrayList();
            for (UrlMetadata urlMetadata : urlMetadataList) {
                if (dao.exists(urlMetadata.getUrl())) continue;
                list.add(urlMetadata);
            }
            dao.save(list);
            log.debug("save {}", list.size());
        }

        UrlMetadata urlMetadata = resultItems.get(UrlMetadata.class.getSimpleName());
        if (urlMetadata != null) {
            if (dao.exists(urlMetadata.getUrl())) return;
            dao.save(urlMetadata);
            log.debug("save {}", urlMetadata);
        }

    }
}
