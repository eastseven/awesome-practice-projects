package cn.eastseven.crud.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;
import java.util.Date;

@Data@Builder@NoArgsConstructor@AllArgsConstructor
@Entity
@Table(name = "t_customer")
public class Customer {

    @JsonView(DataTablesOutput.View.class)
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    private String username;

    private String password;

    @JsonView(DataTablesOutput.View.class)
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonView(DataTablesOutput.View.class)
    private String firstName;

    @JsonView(DataTablesOutput.View.class)
    private String lastName;

    @JsonView(DataTablesOutput.View.class)
    private String mobile;

    @JsonView(DataTablesOutput.View.class)
    private String image = "default.jpg";

    @JsonView(DataTablesOutput.View.class)
    private String email;

    @Enumerated
    @JsonView(DataTablesOutput.View.class)
    private Status status = Status.ENABLED;

    public enum Status {
        ENABLED(1), DISABLED(0);

        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @JsonValue
        public String getText() {
            switch (this.code) {
                case 0: return "无效";
                case 1: return "有效";
            }

            return "未知";
        }
    }
}
