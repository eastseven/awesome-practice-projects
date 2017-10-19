package cn.eastseven.webcrawler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "statistics_gong_gong_zi_yuan")
public class GongGongZiYuanStatistics {

    @Id
    private String date;

    private long totalSize;

    private long totalPage;

    private String datetime;
}
