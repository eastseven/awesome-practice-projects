package ${packageName};

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.RowMapper;

public class ${className}RowMapper implements RowMapper<${className}> {

    @Override
    public ${className} mapRow(Result result, int rowNum) throws Exception {
        return ${className}.builder().build();
    }
}