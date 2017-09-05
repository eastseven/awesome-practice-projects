package cn.eastseven.hadoop.tools;

import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class CodeGenerator {

    @Value("${gen.entity}")
    String entity;

    public void start() throws IOException, TemplateException {

        File dir = Paths.get("src/main/resources/tpl").toFile();

        Configuration cfg = new Configuration(Configuration.getVersion());

        cfg.setDirectoryForTemplateLoading(dir);
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
        Entity root = JSON.parseObject(entity, Entity.class);
        log.debug("{}", root);

        for (String templateName : dir.list()) {
            log.debug("{}", templateName);
            Template template = cfg.getTemplate(templateName);

            String filename = root.getClassName() + templateName.substring(0, templateName.indexOf('.')) + ".java";
            if (filename.endsWith("Entity.java")) filename = filename.replaceAll("Entity", "");

            Path codeFile = Paths.get("target", filename);
            FileWriter writer = new FileWriter(codeFile.toFile());
            template.process(root, writer);
            writer.close();

        }
    }
}
