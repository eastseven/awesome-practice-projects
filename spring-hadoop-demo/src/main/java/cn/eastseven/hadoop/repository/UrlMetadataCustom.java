package cn.eastseven.hadoop.repository;

import cn.eastseven.hadoop.model.UrlMetadata;

public interface UrlMetadataCustom {

    boolean createTable(UrlMetadata urlMetadata);
}
