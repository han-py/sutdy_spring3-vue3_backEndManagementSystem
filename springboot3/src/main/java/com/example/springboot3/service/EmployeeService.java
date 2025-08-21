package com.example.springboot3.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot3.entity.Account;
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
        String username = employee.getUsername();  // 账号
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee != null) {  // 注册的账号已存在  无法注册
            throw new CustomException("500", "账号已存在，请更换别的账号");
        }
        if (StrUtil.isBlank(employee.getPassword())) {  // 密码没填
            employee.setPassword("123");  // 默认密码 123
        }
        if (StrUtil.isBlank(employee.getName())) {  // 名字没填
            employee.setName(employee.getUsername());  // 默认名称
        }
        // 一定要设置角色
        employee.setRole("EMP");  // 员工的角色
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

    public Employee login(Account employee) {
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
