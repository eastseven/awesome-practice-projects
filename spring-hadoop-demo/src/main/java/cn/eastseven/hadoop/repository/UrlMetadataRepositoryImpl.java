package cn.eastseven.hadoop.repository;

import cn.eastseven.hadoop.model.UrlMetadata;
import cn.eastseven.hadoop.service.HBaseService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
    }

    @Override
    public <S extends UrlMetadata> S save(S entity) {
        String rowName = UUID.randomUUID().toString();
        entity.setId(rowName);

        Class clz = entity.getClass();
        for (Field field : clz.getDeclaredFields()) {
            if (!Modifier.isPrivate(field.getModifiers())) continue;
            String qualifier = field.getName();
            byte[] value = null;
            try {
                field.setAccessible(true);
                Object object = field.get(entity);
                value = object != null ? object.toString().getBytes() : null;
            } catch (IllegalAccessException e) {
                log.error("", e);
            } finally {
                field.setAccessible(false);
            }

            hbaseTemplate.put(UrlMetadata.TABLE_NAME, rowName, UrlMetadata.FAMILY_NAME, qualifier, value);
        }
        return entity;
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
    public boolean createTable() {
        return hbaseService.createTable(UrlMetadata.TABLE_NAME, new String[]{UrlMetadata.FAMILY_NAME});
    }
}
