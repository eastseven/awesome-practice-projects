package cn.eastseven.hadoop.repository;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.service.HBaseService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Slf4j
@Repository
@Order
public class UrlMetadataRepositoryImpl implements UrlMetadataRepository, UrlMetadataCustom {

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Autowired
    HBaseService hbaseService;

    public UrlMetadataRepositoryImpl() {
        log.debug("HBaseService {}", hbaseService);
        try {
            if (!hbaseService.isTableExist(UrlMetadata.TABLE_NAME)) {
                hbaseService.createTable(UrlMetadata.TABLE_NAME, new String[]{UrlMetadata.FAMILY_NAME});
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public <S extends UrlMetadata> S save(S entity) {
        String rowKey = UUID.randomUUID().toString();
//        hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowKey, UrlMetadata.FAMILY_NAME, "url", "http://blog.eastseven.cn".getBytes());
//        hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowKey, UrlMetadata.FAMILY_NAME, "text", "Hello HBase".getBytes());
//        hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowKey, UrlMetadata.FAMILY_NAME, "origin", "http://www.eastseven.cn".getBytes());
//        hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowKey, UrlMetadata.FAMILY_NAME, "level", "0".getBytes());
//        hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowKey, UrlMetadata.FAMILY_NAME, "createTime", "2017-09-01 18:05:50".getBytes());
        return null;
    }

    @Override
    public <S extends UrlMetadata> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public UrlMetadata findOne(String s) {
        return hbaseTemplate.get(UrlMetadata.TABLE_NAME, s, UrlMetadata.FAMILY_NAME, new UrlMetadataRowMapper());
    }

    @Override
    public boolean exists(String s) {
        return findOne(s) != null;
    }

    @Override
    public Iterable<UrlMetadata> findAll() {
        return hbaseTemplate.find(UrlMetadata.TABLE_NAME, UrlMetadata.FAMILY_NAME, new UrlMetadataRowMapper());
    }

    @Override
    public Iterable<UrlMetadata> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return Lists.newArrayList(findAll()).size();
    }

    @Override
    public void delete(String s) {
        hbaseTemplate.delete(UrlMetadata.TABLE_NAME, s, UrlMetadata.FAMILY_NAME);
    }

    @Override
    public void delete(UrlMetadata entity) {
        hbaseTemplate.delete(UrlMetadata.TABLE_NAME, entity.getId(), UrlMetadata.FAMILY_NAME);
    }

    @Override
    public void delete(Iterable<? extends UrlMetadata> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean createTable(UrlMetadata urlMetadata) {

        return false;
    }
}
