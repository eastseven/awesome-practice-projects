package cn.eastseven.admin.web;

import cn.eastseven.admin.model.Book;
import cn.eastseven.admin.repository.BookRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class DataTablesController {

    @Autowired
    BookRepository bookRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/books", method = RequestMethod.POST)
    public DataTablesOutput<Book> getBooks(@Valid @RequestBody DataTablesInput input) {
        return bookRepository.findAll(input);
    }

}
