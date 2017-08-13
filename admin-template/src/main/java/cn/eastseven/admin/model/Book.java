package cn.eastseven.admin.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_book")
public class Book {

    @Id@JsonView(DataTablesOutput.View.class)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    private String name;

    @JsonView(DataTablesOutput.View.class)
    private String price;

    @JsonView(DataTablesOutput.View.class)
    private String originPrice;

    @JsonView(DataTablesOutput.View.class)
    private String image;

    @JsonView(DataTablesOutput.View.class)
    @Column(unique = true)
    private String url;

    @JsonView(DataTablesOutput.View.class)
    private String isbn;
}
