package cn.eastseven.demo.crawler.repository;

import cn.eastseven.demo.crawler.model.SpiderData;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author eastseven
 */
public interface SpiderDataRepository extends PagingAndSortingRepository<SpiderData, String> {
}
