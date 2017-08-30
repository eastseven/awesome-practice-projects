package cn.eastseven.web.crawler.repository;

import cn.eastseven.web.crawler.model.Bidding;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BiddingRepository extends MongoRepository<Bidding, String> {

    List<Bidding> findByUrl(String url);
}
