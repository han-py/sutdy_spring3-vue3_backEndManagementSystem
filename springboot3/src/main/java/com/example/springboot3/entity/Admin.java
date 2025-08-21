package com.example.springboot3.entity;

import lombok.Data;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
}
