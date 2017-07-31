package cn.eastseven.crud;

import cn.binarywang.tools.generator.ChineseMobileNumberGenerator;
import cn.eastseven.crud.model.Customer;
import cn.eastseven.crud.repository.CustomerRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class CrudApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CrudApplication.class, args);
    }

    @Autowired
    CustomerRepository customerRepository;

    @Override@Transactional
    public void run(String... strings) throws Exception {

        List<Customer> customers = Lists.newArrayList();
        int size = RandomUtils.nextInt(33, 99);
        for (int index = 0; index < size; index++) {
            Customer customer = Customer.builder()
                    .createTime(DateTime.now().plusMinutes(RandomUtils.nextInt(1, 999999)).toDate())
                    .email(RandomStringUtils.randomAlphabetic(5) + "@test.com")
                    .firstName(RandomStringUtils.randomAlphabetic(5)).lastName(RandomStringUtils.randomAlphabetic(5))
                    .username(RandomStringUtils.randomAlphabetic(5))
                    .mobile(ChineseMobileNumberGenerator.getInstance().generateFake())
                    .status(RandomUtils.nextInt(1, 10) % 2 == 1 ? Customer.Status.ENABLED : Customer.Status.DISABLED)
                    .build();
            customers.add(customer);
        }
        customerRepository.save(customers);
        log.debug("生成测试数据{}条", customers.size());
    }
}
