package cn.eastseven.admin.repository;

import cn.eastseven.admin.model.Book;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;

public interface BookRepository extends DataTablesRepository<Book, Long> {}
