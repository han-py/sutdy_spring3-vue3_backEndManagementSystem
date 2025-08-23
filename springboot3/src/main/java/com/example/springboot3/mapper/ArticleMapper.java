package com.example.springboot3.mapper;

import com.example.springboot3.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {
    List<Article> selectAll(Article article);

    /**
     * 根据id查询员工 使用注解形式查询
     */
    @Select("select * from `article` where id = #{id}")
    Article selectById(Integer id);

    void insert(Article article);

    void updateById(Article article);

    @Delete("delete from `article` where id = #{id}")
    void deleteById(Integer id);

    Article selectByUsername(String username);
}
