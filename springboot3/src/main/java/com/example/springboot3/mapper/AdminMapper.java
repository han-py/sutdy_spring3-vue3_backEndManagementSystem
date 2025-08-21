package com.example.springboot3.mapper;

import com.example.springboot3.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    /**
     * 根据id查询员工 使用注解形式查询
     */
    @Select("select * from `admin` where id = #{id}")
    Admin selectById(Integer id);

    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("delete from `admin` where id = #{id}")
    void deleteById(Integer id);

    Admin selectByUsername(String username);
}
