package cn.eastseven.hadoop.repository;

import cn.eastseven.hadoop.model.UrlMetadata;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.hadoop.hbase.RowMapper;

import java.util.Date;

public class UrlMetadataRowMapper implements RowMapper<UrlMetadata> {

    @Override
    public UrlMetadata mapRow(Result result, int rowNum) throws Exception {
        String createTime = Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "createTime".getBytes()));
        Date date = DateTime.parse(createTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        return UrlMetadata.builder()
                .origin(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "origin".getBytes())))
                .url(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "url".getBytes())))
                .text(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "text".getBytes())))
                .level(Integer.valueOf(Bytes.toString(result.getValue(UrlMetadata.FAMILY_NAME.getBytes(), "level".getBytes()))))
                .createTime(date)
                .build();
    }
}
