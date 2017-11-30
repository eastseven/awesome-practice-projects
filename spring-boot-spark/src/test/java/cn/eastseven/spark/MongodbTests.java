package cn.eastseven.spark;

import com.google.common.collect.Lists;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
@Profile("prod")
public class MongodbTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    private String bid_news_analysis = "bid_news_analysis";
    private String bid_news_company = "bid_news_company";
    private String bid_news_project = "bid_news_project";

    //mvn test -Dtest=MongodbTests#test -Dspring.profiles.active=prod -Paliyun
    @Test
    public void test() {
        Assert.assertNotNull(mongoTemplate);

        Map<String, Long> map = Arrays.asList(bid_news_analysis, bid_news_company, bid_news_project).stream()
                .collect(Collectors.toMap(collection -> collection, collection -> mongoTemplate.getCollection(collection).count()));

        map.forEach((k,v) -> log.debug("{}, {}", k, v));

        log.info(">>> ===== start =====");
        DBCursor cursor = mongoTemplate.getCollection(bid_news_analysis).find();
        log.info(">>> ===== 1 =====");
        List<DBObject> list = Lists.newArrayList(cursor.iterator());
        log.info(">>> ===== 2 =====");
        Set<String> projectIds = list.stream().map(row -> row.get("same_bid_id").toString()).collect(Collectors.toSet());
        log.info(">>> ===== 3 =====");
        List<DBObject> projectList = Lists.newArrayList(mongoTemplate.getCollection(bid_news_project).find().iterator());
        log.info(">>> ===== 4 =====");
        Set<String> allProjectIds = projectList.stream().map(row -> row.get("_id").toString()).collect(Collectors.toSet());
        log.info(">>> ===== 5 =====");
        Set<String> notExistsProjectIds = projectIds.stream().filter(projectId -> !allProjectIds.contains(projectId)).collect(Collectors.toSet());
        log.info(">>> ===== 6 =====");
        log.debug(">>> bid_news_analysis size {}, bid_news_project size {}, not exists size {}", projectIds.size(), allProjectIds.size(), notExistsProjectIds.size());
    }
}
