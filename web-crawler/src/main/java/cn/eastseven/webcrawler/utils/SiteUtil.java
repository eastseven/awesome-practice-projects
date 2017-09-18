package cn.eastseven.webcrawler.utils;

import us.codecraft.webmagic.Site;

public final class SiteUtil {

    final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";

    public static final Site get() {
        return Site.me()
                .setUserAgent(USER_AGENT)
                .setSleepTime(4321)
                .setCycleRetryTimes(3)
                .setTimeOut(30000)
                .setRetrySleepTime(5432)
                .setRetryTimes(3);
    }
}
