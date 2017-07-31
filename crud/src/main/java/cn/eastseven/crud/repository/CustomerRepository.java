package cn.eastseven.crud.repository;

import cn.eastseven.crud.model.Customer;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface CustomerRepository extends DataTablesRepository<Customer, Long> {
}
