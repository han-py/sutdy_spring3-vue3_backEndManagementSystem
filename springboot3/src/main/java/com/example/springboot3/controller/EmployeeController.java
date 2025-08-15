package com.example.springboot3.controller;

import com.example.springboot3.common.Result;
import com.example.springboot3.entity.Employee;
import com.example.springboot3.service.EmpolyeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmpolyeeService empolyeeService;

    /**
     * 查询所有的数据
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Employee> list = empolyeeService.selectAll();
        return Result.success(list);
    }

    /**
     * 通过id查询单个数据
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employee employee = empolyeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 分页查询数据
     * pageNum: 当前页码
     * pageSize: 每页显示的条数
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> pageInfo = empolyeeService.selectPage(pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
