package cn.eastseven.webmagic.urls.master;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author eastseven
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        final String queueName = "spider-urls";
        RabbitTemplate rabbitTemplate = ctx.getBean(RabbitTemplate.class);
        rabbitTemplate.setQueue(queueName);

        Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                List<Map<String, Object>> list = page.getHtml().getDocument().body().select("div.tabct.clearfix ul li").parallelStream()
                        .map(element -> {
                            Map<String, Object> data = Maps.newHashMap();
                            data.put("name", element.select("p.pname").text());
                            data.put("url", element.select("p.pname a").attr("href"));
                            data.put("mPrice", element.select("p:nth-child(3) > span:nth-child(1)").text());
                            data.put("price", element.select("p:nth-child(3) > span:nth-child(2)").text());
                            return data;
                        }).collect(Collectors.toList());

                int index = 1;
                for (Map<String, Object> data : list) {
                    data.put("id", "NO."+index);
                    index++;
                }

                page.putField("list", list);
            }

            @Override
            public Site getSite() {
                return Site.me();
            }
        }).addPipeline(new Pipeline() {
            @Override
            public void process(ResultItems resultItems, Task task) {
                log.info(">>> rabbitTemplate=[{}]", rabbitTemplate);
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> list = resultItems.get("list");
                list.forEach(data -> {
                    try {
                        rabbitTemplate.convertAndSend(queueName, mapper.writeValueAsString(data));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                });

            }
        }).setSpiderListeners(Lists.newArrayList(new SpiderListener() {
            @Override
            public void onSuccess(Request request) {
                log.info(">>> success {}", request.getUrl());
            }

            @Override
            public void onError(Request request) {

            }
        })).addUrl("http://www.china-pub.com/xinshu/").start();
    }

}
