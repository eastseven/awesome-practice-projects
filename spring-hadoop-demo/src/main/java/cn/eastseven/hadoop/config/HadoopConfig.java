package cn.eastseven.hadoop.config;

import cn.eastseven.hadoop.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.hadoop.config.annotation.EnableHadoop;
import org.springframework.data.hadoop.config.annotation.SpringHadoopConfigurerAdapter;
import org.springframework.data.hadoop.config.annotation.builders.HadoopConfigConfigurer;
import org.springframework.data.hadoop.config.annotation.builders.SpringHadoopConfigBuilder;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

@Slf4j
@EnableHadoop
@Configuration
public class HadoopConfig extends SpringHadoopConfigurerAdapter {

    @Override
    public void configure(SpringHadoopConfigBuilder builder) throws Exception {
        super.configure(builder);
    }

    @Override
    public void configure(HadoopConfigConfigurer config) throws Exception {
        super.configure(config);
    }

    @Value("${app.hbase.host}")
    String HOST;
    @Value("${app.hbase.port}")
    String PORT;

    @Bean
    HbaseTemplate hbaseTemplate() {
        return new HbaseTemplate(hbaseConfiguration());
    }

    //@Bean("hbaseConfiguration")
    org.apache.hadoop.conf.Configuration hbaseConfiguration() {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", HOST);
        config.set("hbase.zookeeper.property.clientPort", PORT);
        config.setInt("mapreduce.task.timeout", 1200000);
        config.setInt("hbase.client.scanner.timeout.period", 600000);
        config.setInt("hbase.rpc.timeout", 600000);
        return config;
    }

    @Bean
    HBaseService hbaseService() {
        //return new HBaseService(HOST, PORT);
        return new HBaseService(hbaseConfiguration());
    }
}
