package cn.eastseven.webcrawler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "book_count")
public class BookCount {

    @Id
    private String id; // yyyyMMddhhmm

    private String date, time;

    @Field("china_pub")
    private long chinaPub;

    @Field("dang_dang")
    private long dangDang;

    @Field("win_xuan")
    private long winXuan;
}
