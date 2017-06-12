package cn.eastseven.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dongqi on 17/6/8.
 */
@Data
public class AppClient implements Serializable {

    private Long id;
    private String name;
    private String fullName;
    private Date create = new Date();
}
