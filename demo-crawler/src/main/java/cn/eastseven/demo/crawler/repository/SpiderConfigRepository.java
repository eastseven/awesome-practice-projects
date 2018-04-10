package cn.eastseven.demo.crawler.repository;

import cn.eastseven.demo.crawler.model.SpiderConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author eastseven
 */
public interface SpiderConfigRepository extends PagingAndSortingRepository<SpiderConfig, String> {
}
