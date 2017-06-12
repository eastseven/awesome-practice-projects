package cn.eastseven.mapper;

import cn.eastseven.model.FactWeather;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by dongqi on 17/5/25.
 */
public interface FactWeatherMapper {

    @Select("select * from " + FactWeather.T)
    @Results(value = {
            @Result(property = "dt", column = "dt", javaType = Date.class),
            @Result(property = "windLevel", column = "wind_level"),
            @Result(property = "windDirect", column = "wind_direct"),
            @Result(property = "minTemp", column = "min_temp"),
            @Result(property = "maxTemp", column = "max_temp"),
    })
    List<FactWeather> findAll();

    @Select("select * from " + FactWeather.T)
    @Results(value = {
            @Result(property = "dt", column = "dt", javaType = Date.class),
            @Result(property = "windLevel", column = "wind_level"),
            @Result(property = "windDirect", column = "wind_direct"),
            @Result(property = "minTemp", column = "min_temp"),
            @Result(property = "maxTemp", column = "max_temp"),
    })
    List<FactWeather> page(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
