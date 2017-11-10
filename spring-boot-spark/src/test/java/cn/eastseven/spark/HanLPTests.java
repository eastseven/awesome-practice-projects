package cn.eastseven.spark;

import com.hankcs.hanlp.HanLP;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@Slf4j
public class HanLPTests {

    @Test
    public void test() {
        String content = "程序员(英文Programmer)是从事程序开发、维护的专业人员。" +
                "一般将程序员分为程序设计人员和程序编码人员，" +
                "但两者的界限并不非常清楚，特别是在中国。" +
                "软件从业人员分为初级程序员、高级程序员、系统" +
                "分析员和项目经理四大类。";
        List<String> keywordList = HanLP.extractKeyword(content, 10);
        Assert.assertNotNull(keywordList);
        log.debug("{}", keywordList);
    }
}
