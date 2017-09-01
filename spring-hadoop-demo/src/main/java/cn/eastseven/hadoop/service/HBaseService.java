package cn.eastseven.hadoop.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;

@Slf4j
public class HBaseService {

    private HBaseAdmin admin;
    private Connection connection;
    private Configuration conf;

    public HBaseService(Configuration conf) {
        if (admin == null) {
            try {
                connection = ConnectionFactory.createConnection(conf);
                admin = (HBaseAdmin) connection.getAdmin();
            } catch (MasterNotRunningException e) {
                e.printStackTrace();
            } catch (ZooKeeperConnectionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HBaseService(String host, String port) {
        if (admin == null) {
            conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", host);
            conf.set("hbase.zookeeper.property.clientPort", port);
            conf.setInt("mapreduce.task.timeout", 1200000);
            conf.setInt("hbase.client.scanner.timeout.period", 600000);
            conf.setInt("hbase.rpc.timeout", 600000);
            try {
                connection = ConnectionFactory.createConnection(conf);
                admin = (HBaseAdmin) connection.getAdmin();
            } catch (MasterNotRunningException e) {
                e.printStackTrace();
            } catch (ZooKeeperConnectionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Configuration getConfiguration() {
        return conf;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     */
    public Boolean isTableExist(String tableName) {
        try {
            if (admin.tableExists(tableName)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取TableDescriptor列表
     *
     * @return TableDescriptor列表
     */
    public HTableDescriptor[] getTableList() {
        try {
            return admin.listTables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取TableDescriptor列表
     *
     * @return TableDescriptor列表
     */
    public Set<String> getTableNameList() {
        Set<String> result = new HashSet<>();
        try {
            HTableDescriptor[] tables = admin.listTables();

            for (int i = 0; i < tables.length; i++) {
                result.add(tables[i].getNameAsString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param family    列簇数组
     */
    public boolean createTable(String tableName, String[] family) {
        try {
            HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
            for (String f : family) {
                desc.addFamily(new HColumnDescriptor(f));
            }
            if (!admin.tableExists(tableName)) {
                admin.createTable(desc);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     */
    public Boolean dropTable(String tableName) {
        try {
            if (admin.tableExists(tableName)) {
                admin.disableTable(tableName);
                admin.deleteTable(tableName);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 插入一条数据
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列簇
     * @param qualifier 子列
     * @param value     值
     * @return 是否插入成功
     */
    public boolean insert(String tableName, String rowKey, String family, String qualifier, String value) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Put put = new Put(rowKey.getBytes());
            put.addColumn(family.getBytes(), qualifier.getBytes(), value.getBytes());
            table.put(put);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据主键删除一行数据
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @return 是否删除成功
     */
    public boolean deleteByRowkey(String tableName, String rowKey) {
        return delete(tableName, rowKey, null, null);
    }

    /**
     * 删除一行中的某个列簇
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列簇
     * @return 是否删除成功
     */
    public boolean deleteFamily(String tableName, String rowKey, String family) {
        return delete(tableName, rowKey, family, null);
    }

    /**
     * 删除数据
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列簇
     * @param qualifier 子列
     * @return 是否删除成功
     */
    public boolean delete(String tableName, String rowKey, String family, String qualifier) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Delete del = new Delete(rowKey.getBytes());
            if (qualifier != null) {
                del.addColumn(family.getBytes(), qualifier.getBytes());
            } else if (family != null) {
                del.addFamily(family.getBytes());
            }
            table.delete(del);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除表中某个列修饰符
     *
     * @param tableName 表名
     * @param family    列簇名
     * @param qualifier 列修饰符名
     * @return 是否删除成功
     */
    public boolean deleteColumn(String tableName, String family, String qualifier) {
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            ResultScanner resultScanner = table.getScanner(new Scan());
            for (Result result : resultScanner) {
                Delete delete = new Delete(result.getRow());
                delete.addColumns(Bytes.toBytes(family), Bytes.toBytes(qualifier), System.currentTimeMillis());
                table.delete(delete);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取列簇
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @param family    列簇
     * @return 列族内容
     */
    public Map<String, String> get(String tableName, String rowKey, String family) {
        Map<String, String> result = null;
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(rowKey.getBytes());
            get.addFamily(family.getBytes());
            Result r = table.get(get);
            List<Cell> cells = r.listCells();
            if (cells.size() == 0) {
                return null;
            }
            result = new HashMap<String, String>();
            for (Cell cell : cells) {
                result.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取行数据
     *
     * @param tableName 表名
     * @param rowKey    主键
     * @return 行数据
     */
    public Map<String, Map<String, String>> get(String tableName, String rowKey) {
        Map<String, Map<String, String>> result = null;
        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(Bytes.toBytes(rowKey));
            Result r = table.get(get);
            List<Cell> cells = r.listCells();
            if (cells.size() == 0) {
                return null;
            }
            result = new HashMap<String, Map<String, String>>();
            for (Cell cell : cells) {
                String familyName = Bytes.toString(CellUtil.cloneFamily(cell));
                if (result.get(familyName) == null) {
                    result.put(familyName, new HashMap<String, String>());
                }
                result.get(familyName).put(
                        Bytes.toString(CellUtil.cloneQualifier(cell)),
                        Bytes.toString(CellUtil.cloneValue(cell)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据正则表达式查询指定列中的值
     *
     * @param tableName   表名
     * @param family      列族
     * @param column      列名
     * @param regexString 正则表达式
     * @return 值列表
     * @throws IOException
     */
    public List<String> queryColumnValueByRegex(String tableName, String family, String column, String regexString) throws IOException {

        List<String> values = new ArrayList<>();

        //set scan
        Scan scan = new Scan();
        RegexStringComparator comp = new RegexStringComparator(regexString);
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comp);
        scan.setFilter(filter);
        scan.setCaching(200);
        scan.setCacheBlocks(false);

        //get table & scan
        Table table = connection.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(scan);
        byte[] familyByte = Bytes.toBytes(family);
        byte[] columnByte = Bytes.toBytes(column);
        for (Result result : scanner) {
            String value = Bytes.toString(result.getValue(familyByte, columnByte));
            values.add(value);
        }

        return values;
    }

    /**
     * 扫描表中制定的列簇下面的列修饰符
     *
     * @param tableName 表名
     * @param family    列簇
     * @param columns   列
     * @return List<String[]> 列簇值列表
     * @throws IOException
     */
    public List<String[]> scan(String tableName, String family, String[] columns) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        List<String[]> list = new ArrayList<String[]>();
        ResultScanner resultScanner = table.getScanner(new Scan());
        for (Result result : resultScanner) {
            String rowkey = new String(result.getRow());
            String[] rows = new String[rowkey.length() + 1];
            rows[0] = rowkey;
            for (int i = 0; i < columns.length; i++) {
                rows[i + 1] = new String(result.getValue(Bytes.toBytes(family), Bytes.toBytes(columns[i])));
            }
            list.add(rows);
        }
        return list;
    }

    /**
     * 关闭连接
     */
    public Boolean close() {
        if (admin != null) {
            try {
                admin.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
