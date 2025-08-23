package com.example.springboot3.mapper;

import com.example.springboot3.entity.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DeptMapper {
    List<Dept> selectAll(Dept dept);

    /**
     * 根据id查询员工 使用注解形式查询
     */
    @Select("select * from `dept` where id = #{id}")
    Dept selectById(Integer id);

    void insert(Dept dept);

    void updateById(Dept dept);

    @Delete("delete from `dept` where id = #{id}")
    void deleteById(Integer id);
}
