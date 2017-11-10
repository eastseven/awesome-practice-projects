package cn.eastseven.spark;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class ExcelTests {

    @Test
    public void test() throws Exception {
        String path = "/Users/dongqi/Downloads/行业分类.xlsx";
        Assert.assertNotNull(path);
        log.info("{}", path);


    }
}
