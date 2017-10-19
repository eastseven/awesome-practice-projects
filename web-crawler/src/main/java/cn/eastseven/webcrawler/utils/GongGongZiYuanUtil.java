package cn.eastseven.webcrawler.utils;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Map;

@Slf4j
public final class GongGongZiYuanUtil {

    static final String YYYYMMDD = "yyyy-MM-dd";

    public static Map<String, Object> getPostPageParams() {
        return getPostPageParams(DateTime.now().minusDays(1).toString(YYYYMMDD));
    }

    public static Map<String, Object> getPostPageParams(String start) {
        return getPostPageParams(start, null);
    }

    public static Map<String, Object> getPostPageParams(String start, String end) {
        if (StringUtils.isBlank(start)) {
            start = DateTime.now().minusDays(1).toString(YYYYMMDD);
        }

        if (StringUtils.isBlank(end)) {
            end = DateTime.now().toString(YYYYMMDD);
        }

        Map<String, Object> param = Maps.newHashMap();
        param.put("TIMEBEGIN", start);
        param.put("TIMEEND", end);
        param.put("DEAL_TIME", "06");
        param.put("DEAL_CLASSIFY", "00");
        param.put("DEAL_STAGE", "0000");
        param.put("DEAL_PROVINCE", 0);
        param.put("DEAL_CITY", 0);
        param.put("DEAL_PLATFORM", 0);
        param.put("DEAL_TRADE", 0);
        param.put("isShowAll", 0);
        param.put("PAGENUMBER", 1);
        return param;
    }
}
