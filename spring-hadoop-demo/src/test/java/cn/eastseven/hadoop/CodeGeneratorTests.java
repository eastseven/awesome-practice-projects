package cn.eastseven.hadoop;

import cn.eastseven.hadoop.tools.CodeGenerator;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeGeneratorTests {

    @Value("${gen.entity}")
    String entity;

    @Autowired
    CodeGenerator codeGenerator;

    @Test
    public void test() throws IOException, TemplateException {
        Assert.assertNotNull(entity);
        log.debug("{}", entity);

        log.debug("{}", Paths.get("src/main/resources/tpl"));
        codeGenerator.start();
    }
}
