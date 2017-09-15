package cn.eastseven.webcrawler.utils;

import us.codecraft.webmagic.Site;

public final class SiteUtil {

    public static final Site get() {
        return Site.me().setSleepTime(1234)
                .setCycleRetryTimes(3)
                .setTimeOut(4321)
                .setRetrySleepTime(1234)
                .setRetryTimes(3);
    }
}
