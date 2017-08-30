package cn.eastseven.web.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.store.config.annotation.EnableDataStoreTextWriter;
import org.springframework.data.hadoop.store.config.annotation.SpringDataStoreTextWriterConfigurerAdapter;
import org.springframework.data.hadoop.store.config.annotation.builders.DataStoreTextWriterConfigurer;

@Configuration
@EnableDataStoreTextWriter
public class HadoopConfig extends SpringDataStoreTextWriterConfigurerAdapter {

    @Value("${app.hadoop.dir}") String path;

    @Override
    public void configure(DataStoreTextWriterConfigurer writer) throws Exception {
        writer.basePath(path).append(true);
    }

}
