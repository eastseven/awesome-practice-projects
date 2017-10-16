package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.WinXuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WinXuanRepository extends MongoRepository<WinXuan, String> {

    Page<WinXuan> findByCreateTimeIsNull(Pageable pageable);
}
