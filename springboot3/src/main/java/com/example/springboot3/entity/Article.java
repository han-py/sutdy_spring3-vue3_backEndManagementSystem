package com.example.springboot3.entity;

import lombok.Data;

@Data
public class Article {
    private  Integer id;
    private  String title;
    private  String img;
    private  String description;
    private  String content;
    private  String time;
}
