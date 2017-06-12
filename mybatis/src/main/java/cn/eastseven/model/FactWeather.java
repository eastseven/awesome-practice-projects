package cn.eastseven.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by dongqi on 17/5/25.
 * <p>
 * CREATE TABLE `fact_weather_cn` (
 * `area` varchar(50) NOT NULL,
 * `dt` date NOT NULL,
 * `weather` varchar(50) NOT NULL,
 * `min_temp` int(11) NOT NULL,
 * `max_temp` int(11) NOT NULL,
 * `wind_level` varchar(255) NOT NULL,
 * `wind_direct` varchar(255) NOT NULL,
 * PRIMARY KEY (`area`,`dt`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Data
public class FactWeather {

    public static final String T = "fact_weather_cn";

    private String area, weather, windLevel, windDirect;
    private int minTemp, maxTemp;
    private Date dt;
}
