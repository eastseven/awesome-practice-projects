package cn.eastseven.crud.service;

import cn.eastseven.crud.model.Customer;
import cn.eastseven.crud.model.CustomerForm;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

public interface CustomerService {

    DataTablesOutput<Customer> retrieve(DataTablesInput input);

    Customer retrieve(long id);

    void delete(long id) throws Exception;

    void create(CustomerForm form) throws Exception;

    void update(CustomerForm form) throws Exception;
}
