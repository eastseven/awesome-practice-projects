package cn.eastseven.demo.crawler.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author eastseven
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spider_data")
public class SpiderData {

    @Id
    @Column(length = 127)
    private String url;

    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "config_id")
    private SpiderConfig config;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();
}
