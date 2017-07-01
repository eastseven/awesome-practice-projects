package cn.eastseven;

import cn.eastseven.mapper.AreaMapper;
import cn.eastseven.mapper.FactWeatherMapper;
import cn.eastseven.model.Area;
import cn.eastseven.model.FactWeather;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
//@MybatisTest
public class MybatisApplicationTests {

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    FactWeatherMapper factWeatherMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testArea() {
        Assert.assertNotNull(areaMapper);

        List<Area> all = areaMapper.findAll();
        Assert.assertNotNull(all);

        log.debug("area data count={}, {}", all.size(), all.getClass());

        PageHelper.startPage(1, 10);
        List<Area> page = areaMapper.page(Maps.newHashMap());
        Assert.assertNotNull(all);
        Assert.assertEquals(page.size(), 10);
        log.debug("\t->{}, {}", page.getClass(), page);
        page.stream().forEach(System.out::println);

        Arrays.stream(new Integer[]{1, 2, 3}).forEach(pageNum -> {
            PageHelper.startPage(pageNum, 15);
            List<Area> list = areaMapper.page(Maps.newHashMap());
            log.debug("\t->{}", list);
            list.stream().forEach(System.out::println);
        });
    }

    @Test
    public void testAreaPage() {
        Assert.assertNotNull(areaMapper);

        PageHelper.startPage(1, 15);
        List<Area> all = areaMapper.findAll();
        Assert.assertNotNull(all);
        log.debug("{}, {}", all.getClass(), all);
    }

    @Test
    public void testFactWeather() {
        Assert.assertNotNull(factWeatherMapper);

        List<FactWeather> all = factWeatherMapper.findAll();
        Assert.assertNotNull(all);
        //log.debug("{}", all.iterator().next());
        all.subList(0, 6).forEach(System.out::println);
    }
}
