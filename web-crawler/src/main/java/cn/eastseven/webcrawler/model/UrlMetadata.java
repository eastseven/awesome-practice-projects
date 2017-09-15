package cn.eastseven.webcrawler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "url_metadata")
public class UrlMetadata {

    @Id
    @Column(length = 1024)
    //@GeneratedValue
    private String url;

    private String text;
    private String origin;
    private int level = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
}
