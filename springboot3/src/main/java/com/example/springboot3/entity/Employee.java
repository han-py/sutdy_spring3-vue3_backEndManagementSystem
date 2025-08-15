package com.example.springboot3.entity;

import lombok.Data;

@Data
public class Employee {
    private Integer id;
    private String name;
    private String sex;
    private String no;
    private Integer age;
    private String description;
    private Integer departmentId;
}
