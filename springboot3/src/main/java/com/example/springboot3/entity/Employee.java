package com.example.springboot3.entity;

import lombok.Data;

@Data
public class Employee extends Account{
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String name;
    private String sex;
    private String no;
    private Integer age;
    private String description;
    private Integer departmentId;
}
