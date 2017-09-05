package ${packageName};

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

@Slf4j
@Repository
public class ${className}RepositoryImpl implements ${className}Repository {

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Override
    public <S extends ${className}> S save(S entity) {
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

            hbaseTemplate.put(${className}.TABLE_NAME, rowName, ${className}.FAMILY_NAME, qualifier, value);
        }
        return entity;
    }

    @Override
    public <S extends ${className}> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public ${className} findOne(String s) {
        return hbaseTemplate.get(${className}.TABLE_NAME, s, ${className}.FAMILY_NAME, new ${className}RowMapper());
    }

    @Override
    public boolean exists(String s) {
        return findOne(s) != null;
    }

    @Override
    public Iterable<${className}> findAll() {
        return hbaseTemplate.find(${className}.TABLE_NAME, ${className}.FAMILY_NAME, new ${className}RowMapper());
    }

    @Override
    public Iterable<${className}> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return Lists.newArrayList(findAll()).size();
    }

    @Override
    public void delete(String id) {
        hbaseTemplate.delete(${className}.TABLE_NAME, id, ${className}.FAMILY_NAME);
    }

    @Override
    public void delete(${className} entity) {
        hbaseTemplate.delete(${className}.TABLE_NAME, entity.getId(), ${className}.FAMILY_NAME);
    }

    @Override
    public void delete(Iterable<? extends ${className}> entities) {

    }

    @Override
    public void deleteAll() {

    }

}
