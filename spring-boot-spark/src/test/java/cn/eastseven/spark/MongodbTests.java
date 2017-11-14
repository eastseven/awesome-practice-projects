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

    @Test
    public void test() {
        Assert.assertNotNull(mongoTemplate);

        Map<String, Long> map = Arrays.asList(bid_news_analysis, bid_news_company, bid_news_project).stream()
                .collect(Collectors.toMap(collection -> collection, collection -> mongoTemplate.getCollection(collection).count()));

        map.forEach((k,v) -> log.debug("{}, {}", k, v));

        List<String> companyIdList = Lists.newArrayList();
        DBCursor cursor = mongoTemplate.getCollection(bid_news_analysis).find();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            Object id = dbObject.get("_id");
            Object projectId = dbObject.get("same_bid_id");
            if (projectId == null) {
                companyIdList.add(id.toString());
                mongoTemplate.getCollection(bid_news_analysis).remove(dbObject);
                log.debug("remove {} same_bid_id is null in bid_news_company", id);
            }
        }

        log.debug(">>> name is null in bid_news_company, size {}", companyIdList.size());
    }
}
