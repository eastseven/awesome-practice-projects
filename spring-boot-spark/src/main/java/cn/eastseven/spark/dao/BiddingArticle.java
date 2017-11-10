package cn.eastseven.spark.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "bidding_article")
public class BiddingArticle implements Serializable {

    @Id
    private String rowKey;

    private String url;

    private String title;

    private String industry;

    private String code;
}
