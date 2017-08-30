package cn.eastseven.web.crawler.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "bidding_ggzy")
public class Bidding {

    @Id private String id;

    private String url;
    private String title;

    private String date;
    private String province;
    private String origin;
    private String type1;
    private String type2;

    private String html;
}