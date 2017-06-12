package cn.eastseven.mapper;

import cn.eastseven.model.Area;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by dongqi on 17/5/25.
 */
public interface AreaMapper {

    @Select("select count(code) from " + Area.T)
    long count();

    @Select("select * from " + Area.T)
    List<Area> findAll();

    @Select("select * from " + Area.T)
    List<Area> page(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
