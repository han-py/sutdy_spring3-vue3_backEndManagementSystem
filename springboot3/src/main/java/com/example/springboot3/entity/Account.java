package com.example.springboot3.entity;

import lombok.Data;

@Data
public class Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String newPassword = "";
    private String avatar; // 头像路径
}
