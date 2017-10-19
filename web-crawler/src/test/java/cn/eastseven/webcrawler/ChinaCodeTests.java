package cn.eastseven.webcrawler;

import cn.eastseven.webcrawler.model.ChinaCode;
import cn.eastseven.webcrawler.repository.ChinaCodeRepository;
import cn.eastseven.webcrawler.service.ChinaCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChinaCodeTests {

    @Autowired
    ChinaCodeService service;

    @Autowired
    ChinaCodeRepository chinaCodeRepository;

    @Test
    public void test() throws Exception {
        Assert.assertNotNull(service);
        service.start();
    }

    @Test
    public void testFile() throws Exception {
        Assert.assertNotNull(chinaCodeRepository);
        List<ChinaCode> chinaCodeList = chinaCodeRepository.findAll();
        FileWriter writer = new FileWriter("code.txt", true);
        for (ChinaCode code : chinaCodeList) {
            String line = code.getProvince() + ',' + code.getCity() + ',' + code.getCountry() + ',' + code.getTown() + '\n';
            IOUtils.write(line, writer);
        }
        writer.flush();
    }
}
