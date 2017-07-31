package cn.eastseven.crud.model;

import lombok.Data;

@Data
public class CustomerForm {

    private Long id;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String mobile;

    private String image = "default.jpg";

    private String email;

    private Customer.Status status;
}
