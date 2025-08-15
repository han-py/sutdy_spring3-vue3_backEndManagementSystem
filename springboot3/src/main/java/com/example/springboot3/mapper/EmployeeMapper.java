package com.example.springboot3.mapper;

import com.example.springboot3.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    List<Employee> selectAll();

    /**
     * 根据id查询员工 使用注解形式查询
     */
    @Select("select * from employee where id = #{id}")
    Employee selectById(Integer id);
}
