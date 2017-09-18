package cn.eastseven.webcrawler.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

@Slf4j
@Component
public class DataSpiderListener implements SpiderListener {

    @Override
    public void onSuccess(Request request) {

    }

    @Override
    public void onError(Request request) {

    }
}
