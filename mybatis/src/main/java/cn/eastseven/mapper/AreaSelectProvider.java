package cn.eastseven.mapper;

import cn.eastseven.model.Area;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * Created by dongqi on 17/7/1.
 */
@Slf4j
public class AreaSelectProvider {

    public String query(Map<String, Object> params) {
        log.debug("{}", params);
        SQL sql = new SQL().SELECT("*").FROM(Area.T).WHERE("1=1");
        if (params.isEmpty()) return sql.toString();

        params.forEach((key, value) -> {
            switch (key) {
                case "code":
                    sql.AND().WHERE("code = #{code}");
                    break;
                case "region":
                    sql.AND().WHERE("region = #{region}");
                    break;
            }
        });
        return sql.toString();
    }
}
