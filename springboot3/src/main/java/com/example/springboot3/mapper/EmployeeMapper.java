package com.example.springboot3.mapper;

import com.example.springboot3.entity.Employee;

import java.util.List;

public interface EmployeeMapper {
    List<Employee> selectAll();
}
