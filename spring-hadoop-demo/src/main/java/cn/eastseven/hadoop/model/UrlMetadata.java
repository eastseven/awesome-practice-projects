package cn.eastseven.hadoop.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UrlMetadata {

    public static final String TABLE_NAME = "url_metadata";
    public static final String FAMILY_NAME = "f";

    private String id;

    private String url;
    private String text;
    private String origin;
    private int level = 0;
    
    private Date createTime = new Date();
}
