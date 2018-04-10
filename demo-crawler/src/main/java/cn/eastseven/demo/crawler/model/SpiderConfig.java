package cn.eastseven.demo.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author eastseven
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spider_config")
public class SpiderConfig {

    @Id
    private String url;

    /**
     * 目标节点
     */
    private String targetElement;

    /**
     * 目标页面URL正则表达式
     */
    private String regExp;
}
