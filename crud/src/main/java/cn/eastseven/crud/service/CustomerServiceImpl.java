package cn.eastseven.crud.service;

import cn.eastseven.crud.model.Customer;
import cn.eastseven.crud.model.CustomerForm;
import cn.eastseven.crud.model.CustomerSearchForm;
import cn.eastseven.crud.repository.CustomerRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static cn.eastseven.crud.model.Customer.Status.DISABLED;
import static cn.eastseven.crud.model.Customer.Status.ENABLED;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public DataTablesOutput<Customer> retrieve(DataTablesInput input) {
        return customerRepository.findAll(input);
    }

    @Override
    public DataTablesOutput<Customer> retrieve(DataTablesInput input, final CustomerSearchForm searchForm) {
        return customerRepository.findAll(input, new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();
                if (StringUtils.isNotBlank(searchForm.getStatus())) {
                    Customer.Status status = Integer.valueOf(searchForm.getStatus()) == 0 ? DISABLED : ENABLED;
                    predicates.add(criteriaBuilder.equal(root.get("status"), status));
                }

                if (StringUtils.isNotBlank(searchForm.getCompany())) {
                    predicates.add(criteriaBuilder.like(root.get("company"), "%" + searchForm.getCompany() + "%"));
                }

                if (StringUtils.isNotBlank(searchForm.getMobile())) {
                    predicates.add(criteriaBuilder.like(root.get("mobile"), searchForm.getMobile() + "%"));
                }

                if (StringUtils.isNotBlank(searchForm.getName())) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchForm.getName() + "%"));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        });
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
