package cn.eastseven.spark.config;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.Set;

/**
 * @author dongqi
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HBaseConfig implements CommandLineRunner {

    @Value("${hbase.zookeeper.quorum}")
    String quorum;

    @Value("${hbase.namespace}")
    private String namespace;

    @Value("${hbase.table.original}")
    private String TABLE_ORIGINAL;

    @Value("${hbase.table.history}")
    private String TABLE_HISTORY;

    @Value("${hbase.table.analysis}")
    private String TABLE_ANALYSIS;

    @Value("${hbase.table.company}")
    private String TABLE_COMPANY;

    @Value("${hbase.table.project}")
    private String TABLE_PROJECT;

    private Set<TableName> tableNames = Sets.newHashSet();

    public TableName getTableOriginal() {
        return TableName.valueOf(namespace, TABLE_ORIGINAL);
    }

    public TableName getTableHistory() {
        return TableName.valueOf(namespace, TABLE_HISTORY);
    }

    public TableName getTableAnalysis() {
        return TableName.valueOf(namespace, TABLE_ANALYSIS);
    }

    public TableName getTableCompany() {
        return TableName.valueOf(namespace, TABLE_COMPANY);
    }

    public TableName getTableProject() {
        return TableName.valueOf(namespace, TABLE_PROJECT);
    }

    public String getNamespace() {
        return namespace;
    }

    public Set<TableName> getTableNames() {
        return tableNames;
    }

    public org.apache.hadoop.conf.Configuration get() {
        return configuration();
    }

    @Bean
    org.apache.hadoop.conf.Configuration configuration() {
        org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", quorum);
        configuration.setInt("mapreduce.task.timeout", 1200000);
        configuration.setInt("hbase.client.scanner.timeout.period", 600000);
        configuration.setInt("hbase.rpc.timeout", 600000);
        return configuration;
    }

    void init() throws IOException {
        Connection conn = null;
        Admin admin = null;

        try {
            conn = ConnectionFactory.createConnection(configuration());
            admin = conn.getAdmin();
            boolean namespaceExists = false;
            for (NamespaceDescriptor namespaceDescriptor : admin.listNamespaceDescriptors()) {
                if (namespace.equalsIgnoreCase(namespaceDescriptor.getName())) {
                    namespaceExists = true;
                    break;
                }
            }

            if (!namespaceExists) {
                NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
                admin.createNamespace(namespaceDescriptor);
                log.info("create hbase namespace {}", namespace);
            }

            String[] tables = {TABLE_ANALYSIS, TABLE_COMPANY, TABLE_HISTORY, TABLE_ORIGINAL, TABLE_PROJECT};
            for (String table : tables) {
                TableName tableName = TableName.valueOf(namespace, table);
                boolean exists = admin.tableExists(tableName);
                if (!exists) {
                    admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor(Bytes.toBytes("f"))));
                    log.info("create hbase table {}", table);
                }

                tableNames.add(tableName);
            }

        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (admin != null) admin.close();
            if (conn != null) conn.close();
        }
    }

    @Override
    public void run(String... strings) throws Exception {
        // 启动检查
        init();

    }
}
