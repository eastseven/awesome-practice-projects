package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.BookCount;
import cn.eastseven.webcrawler.repository.BookCountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebCrawlerStatistics {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    BookCountRepository bookCountRepository;

    public void count() {
        DateTime dt = DateTime.now().minusMinutes(1);

        String id = dt.toString("yyyyMMddHHmm");
        String date = StringUtils.substring(id, 0, 8);
        String time = StringUtils.substring(id, 8);

        DateTime current = DateTime.parse(id, DateTimeFormat.forPattern("yyyyMMddHHmm"));
        long start = current.getMillis();
        long end = current.plusSeconds(59).getMillis();

        BookCount bookCount = count(start, end);
        bookCount.setId(id);
        bookCount.setDate(date);
        bookCount.setTime(time);
        bookCountRepository.save(bookCount);
        log.info("{}", bookCount);
    }

    public BookCount count(long start, long end) {
        log.debug("query where {} <= createTime <= {}", start, end);
        Query query = new Query(Criteria.where("createTime").gte(start).lte(end));
        long chinaPub = mongoTemplate.count(query, "book_china_pub");
        long dangDang = mongoTemplate.count(query, "book_dang_dang");
        long winXuan = mongoTemplate.count(query, "book_wen_xuan");

        BookCount bookCount = new BookCount();
        bookCount.setChinaPub(chinaPub);
        bookCount.setDangDang(dangDang);
        bookCount.setWinXuan(winXuan);

        return bookCount;
    }
}
