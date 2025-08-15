package com.example.springboot3.controller;

import com.example.springboot3.common.Result;
import com.example.springboot3.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class WebController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/weather")
    public Result weather() {
        return Result.success("今天天气：晴 23摄氏度");
    }

    @GetMapping("/count")
    public Result count() {
        throw new CustomException("400", "错误！禁止请求");
//        throw new RuntimeException("错误！禁止请求");
    }

    @GetMapping("/map")
    public Result map() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "HaPpY");
        map.put("age", 20);
        return Result.success(map);
    }
}
