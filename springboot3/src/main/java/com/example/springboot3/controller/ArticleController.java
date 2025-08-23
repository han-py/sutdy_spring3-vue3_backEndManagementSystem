package com.example.springboot3.controller;

import com.example.springboot3.common.Result;
import com.example.springboot3.entity.Article;
import com.example.springboot3.service.ArticleService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 添加数据
     */
    @PostMapping("/add")
    public Result add(@RequestBody Article article) {
        articleService.add(article);
        return Result.success();
    }

    /**
     * 修改数据
     */
    @PutMapping("/update")
    public Result update(@RequestBody Article article) {
        articleService.update(article);
        return Result.success();
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        articleService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result delete(@RequestBody List<Integer> ids) {
        articleService.deleteBatch(ids);
        return Result.success();
    }

     /**
     * 查询所有的数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(Article article) {
        List<Article> list = articleService.selectAll(article);
        return Result.success(list);
    }

    /**
     * 通过id查询单个数据
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Article article = articleService.selectById(id);
        return Result.success(article);
    }

    /**
     * 分页查询数据
     * pageNum: 当前页码
     * pageSize: 每页显示的条数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Article article,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Article> pageInfo = articleService.selectPage(article,pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
