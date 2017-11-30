package cn.eastseven.crud.repository;

import cn.eastseven.crud.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface CustomerRepository extends DataTablesRepository<Customer, Long> {

    Page<Customer> findByNameLike(String name, Pageable pageable);
}
