package cn.eastseven.webcrawler.pipeline;

import cn.eastseven.webcrawler.model.ChinaPub;
import cn.eastseven.webcrawler.model.DangDang;
import cn.eastseven.webcrawler.model.WinXuan;
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

    @Override
    public void process(Object o, Task task) {
        //log.debug("= {} = {}", o.getClass(), o);

        if (o instanceof DangDang) {
            DangDang dangDang = dangDangRepository.save((DangDang) o);
            log.info("当当 {}, {}, {}", dangDang.getName(), dangDang.getPrice(), dangDang.getUrl());
        }

        if (o instanceof ChinaPub) {
            ChinaPub chinaPub = chinaPubRepository.save((ChinaPub) o);
            log.info("互动 {}, {}, {}", chinaPub.getName(), chinaPub.getPrice(), chinaPub.getUrl());
        }

        if (o instanceof WinXuan) {
            WinXuan winXuan = winXuanRepository.save((WinXuan) o);
            log.info("文轩 {}, {}, {}", winXuan.getName(), winXuan.getPrice(), winXuan.getUrl());
        }
    }
}
