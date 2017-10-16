package cn.eastseven.webcrawler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "book_category")
public class BookCategory {

    @Id
    private String url;

    private String name;

    private BookOrigin origin;
}
