package cn.eastseven.model;

import cn.eastseven.datatables.mapping.DataTablesOutput;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * Created by dongqi on 17/5/25.
 * <p>
 * CREATE TABLE `dim_area_cn` (
 * `code` varchar(6) NOT NULL,
 * `capital` char(1) DEFAULT NULL,
 * `region` varchar(10) DEFAULT NULL,
 * `province` varchar(50) DEFAULT NULL,
 * `city` varchar(50) DEFAULT NULL,
 * `district` varchar(50) DEFAULT NULL,
 * PRIMARY KEY (`code`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
@Data
public class Area {

    public static final String T = "dim_area_cn";

    @JsonView(DataTablesOutput.View.class)
    private String code;

    @JsonView(DataTablesOutput.View.class)
    private String capital;

    @JsonView(DataTablesOutput.View.class)
    private String region;

    @JsonView(DataTablesOutput.View.class)
    private String province;

    @JsonView(DataTablesOutput.View.class)
    private String city;

    @JsonView(DataTablesOutput.View.class)
    private String district;
}
