package cn.eastseven.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by dongqi on 17/5/25.
 * <p>
 * CREATE TABLE `fact_air_cn` (
 * `area` varchar(20) NOT NULL,
 * `dt` date NOT NULL,
 * `aqi` int(11) NOT NULL,
 * `aqi_range` varchar(20) NOT NULL,
 * `quality` varchar(10) NOT NULL,
 * `pm25` float NOT NULL,
 * `pm10` float NOT NULL,
 * `so2` float NOT NULL,
 * `co` float NOT NULL,
 * `no2` float NOT NULL,
 * `o3` float NOT NULL,
 * PRIMARY KEY (`area`,`dt`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Data
public class FactAir {

    public static final String T = "fact_air_cn";

    private String area, aqiRange, quality;
    private Date date;
    private int aqi;
    private float pm25, pm10, so2, co, no2, o3;
}
