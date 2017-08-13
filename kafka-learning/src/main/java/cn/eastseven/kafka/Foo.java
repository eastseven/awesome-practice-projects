package cn.eastseven.kafka;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data@Builder
public class Foo implements Serializable {

    private Long id;

    private String name;

    private Date time = new Date();

    private BigDecimal number;
}
