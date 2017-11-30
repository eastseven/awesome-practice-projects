package cn.eastseven.spark;

import cn.eastseven.spark.service.SparkService;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SparkStatisticsTests implements Serializable {

    final String path = "spark-warehouse/bidding_titles";

    @Autowired
    SparkService service;

    @Test
    public void test() {
        assertNotNull(path);

        JavaRDD<Integer> rdd = service.getJavaSparkContext().parallelize(Lists.newArrayList(1, 2, 3)).cache();
        List<Integer> result = rdd.flatMap(new FlatMapFunctionImpl()).collect();

        log.info(">>> {}", result);
    }

    class FlatMapFunctionImpl implements FlatMapFunction<Integer, Integer>, Serializable {

        @Override
        public Iterator<Integer> call(Integer integer) throws Exception {
            List<Integer> list = new ArrayList();
            list.add(integer + integer);
            list.add(integer + 1);
            return list.iterator();
        }
    }
}
