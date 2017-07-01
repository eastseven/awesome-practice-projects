package cn.eastseven.mapper;

import cn.eastseven.model.Area;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by dongqi on 17/5/25.
 */
public interface AreaMapper {

    @Select("select count(code) from " + Area.T)
    long count();

    @Select("select * from " + Area.T)
    List<Area> findAll();

    //@Select("select * from " + Area.T)
    @SelectProvider(type = AreaSelectProvider.class, method = "query")
    List<Area> page(Map<String, Object> params);

    @Select("select region from " + Area.T + " group by region")
    List<String> groupByRegion();

    @Select("select province from " + Area.T + " group by province")
    List<String> groupByProvince();
}
