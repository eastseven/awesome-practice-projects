package cn.eastseven.webcrawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShellCmdTests {

    @Test
    public void test() throws Exception {
        String cmd = "ls -l /Users/dongqi/Dev/code/my-projects/web-crawler";
        Assert.assertNotNull(cmd);
        log.debug("cmd=[{}]", cmd);

        Process process = Runtime.getRuntime().exec(cmd);
        boolean bln = process.waitFor(30, TimeUnit.SECONDS);
        log.debug("waitFor {}", bln);
        List result = IOUtils.readLines(process.getInputStream());
        Assert.assertNotNull(result);
        log.debug("{}", result);

        for (Object line : result) {
            log.debug(">>>\t{}\t<<<", line);
        }
    }
}
