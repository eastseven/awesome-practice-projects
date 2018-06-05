package cn.eastseven.demo.crawler.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
@ApiModel
public class SpiderConfig {

    @ApiModelProperty("目标网站网址")
    @Id@Column(length = 127)
    private String url;

    /**
     * 目标节点
     */
    @ApiModelProperty("目标网页节点，css selector 语法")
    private String targetElement;

    /**
     * 目标页面URL正则表达式
     */
    @ApiModelProperty("目标页面URL正则表达式")
    private String regExp;

    @ApiModelProperty("关键字，以空格分隔")
    private String keywords;
}
