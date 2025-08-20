package com.example.springboot3.service;

import com.example.springboot3.entity.Employee;
import com.example.springboot3.exception.CustomException;
import com.example.springboot3.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Resource
    EmployeeMapper employeeMapper;

    public List<Employee> selectAll(Employee  employee) {
        return employeeMapper.selectAll(employee);
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Employee employee,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    public void add(Employee employee) {
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void delete(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public Employee login(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee == null) {
            throw new CustomException("500", "账号不存在");
        }
        String password = employee.getPassword();
        if (!password.equals(dbEmployee.getPassword())) {
            throw new CustomException("500", "账号或密码错误");
        }
        return dbEmployee;
    }
}
