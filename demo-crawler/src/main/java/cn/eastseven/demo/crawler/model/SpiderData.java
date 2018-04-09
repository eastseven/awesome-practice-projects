package cn.eastseven.demo.crawler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author eastseven
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "spider_data")
public class SpiderData {

    @Id
    private String url;

    private String title;

    private String content;

    @DBRef
    private SpiderConfig config;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime = new Date();
}
