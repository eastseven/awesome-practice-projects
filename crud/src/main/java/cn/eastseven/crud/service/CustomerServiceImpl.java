package cn.eastseven.crud.service;

import cn.eastseven.crud.model.Customer;
import cn.eastseven.crud.model.CustomerForm;
import cn.eastseven.crud.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public DataTablesOutput<Customer> retrieve(DataTablesInput input) {
        return customerRepository.findAll(input);
    }

    @Override
    public Customer retrieve(long id) {
        return customerRepository.findOne(id);
    }

    @Override
    public void delete(long id) throws Exception {
        if (!customerRepository.exists(id)) throw new Exception("data not exists");

        customerRepository.delete(id);
    }

    @Override
    public void create(CustomerForm form) {
        Customer customer = Customer.builder().build();
        BeanUtils.copyProperties(form, customer);
        log.debug("{}", customer);

        customer.setCreateTime(new Date());
        customer.setStatus(Customer.Status.ENABLED);

        save(customer);
    }

    @Override
    public void update(CustomerForm form) {
        Customer customer = customerRepository.findOne(form.getId());
        BeanUtils.copyProperties(form, customer, "id", "createTime", "password");
        save(customer);
    }

    void save(Customer customer) {
        customerRepository.save(customer);
    }
}
