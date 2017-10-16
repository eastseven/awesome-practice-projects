package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.ChinaPub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChinaPubRepository extends MongoRepository<ChinaPub, String> {

    Page<ChinaPub> findByCreateTimeIsNull(Pageable pageable);
}
