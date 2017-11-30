package cn.eastseven.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author eastseven
 */
public class HBaseConfig {

    private Configuration hbaseConfig;

    public Configuration getHBaseConfig() {
        return hbaseConfig;
    }

    public HBaseConfig() {
        hbaseConfig = createNew();
    }

    public Configuration createNew() {
        Configuration hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.zookeeper.quorum", "s94,s95,s96");
        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        hbaseConfig.setInt("mapreduce.task.timeout", 1200000);
        hbaseConfig.setInt("hbase.client.scanner.timeout.period", 600000);
        hbaseConfig.setInt("hbase.rpc.timeout", 600000);

        return hbaseConfig;
    }

    public Configuration createNew(TableName tableName) throws IOException {
        return createNew(tableName, newScan());
    }

    public Configuration createNew(TableName tableName, Scan scan) throws IOException {
        Configuration hConf = createNew();
        hConf.set(TableInputFormat.INPUT_TABLE, tableName.getNameWithNamespaceInclAsString());
        hConf.set(TableInputFormat.SCAN, Base64.encodeBytes(ProtobufUtil.toScan(scan).toByteArray()));
        hConf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "f");
        return hConf;
    }

    public Scan newScan() {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        filterList.addFilter(new FirstKeyOnlyFilter());
        Scan scan = new Scan();
        scan.addFamily(Bytes.toBytes("f"));

        return scan;
    }
}
