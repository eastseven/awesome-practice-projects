package cn.eastseven.webcrawler.repository;

import cn.eastseven.webcrawler.model.UrlMetadata;
import org.springframework.data.repository.CrudRepository;

public interface UrlMetadataRepository extends CrudRepository<UrlMetadata, String> {
}
