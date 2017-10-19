package cn.eastseven.webcrawler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "china_code")
public class ChinaCode {

    @Id
    private String id;

    private String province, city, country, town;
}
