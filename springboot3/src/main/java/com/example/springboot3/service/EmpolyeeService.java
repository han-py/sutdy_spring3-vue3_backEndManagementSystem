package com.example.springboot3.service;

import com.example.springboot3.entity.Employee;
import com.example.springboot3.mapper.EmployeeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpolyeeService {

    @Resource
    EmployeeMapper employeeMapper;

    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }
}
