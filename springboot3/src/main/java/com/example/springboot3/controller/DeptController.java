package com.example.springboot3.controller;

import com.example.springboot3.common.Result;
import com.example.springboot3.entity.Dept;
import com.example.springboot3.service.DeptService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Resource
    private DeptService deptService;

    /**
     * 添加数据
     */
    @PostMapping("/add")
    public Result add(@RequestBody Dept dept) {
        deptService.add(dept);
        return Result.success();
    }

    /**
     * 修改数据
     */
    @PutMapping("/update")
    public Result update(@RequestBody Dept dept) {
        deptService.update(dept);
        return Result.success();
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        deptService.delete(id);
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result delete(@RequestBody List<Integer> ids) {
        deptService.deleteBatch(ids);
        return Result.success();
    }

     /**
     * 查询所有的数据
     */
    @GetMapping("/selectAll")
    public Result selectAll(Dept dept) {
        List<Dept> list = deptService.selectAll(dept);
        return Result.success(list);
    }

    /**
     * 通过id查询单个数据
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Dept dept = deptService.selectById(id);
        return Result.success(dept);
    }

    /**
     * 分页查询数据
     * pageNum: 当前页码
     * pageSize: 每页显示的条数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Dept dept,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Dept> pageInfo = deptService.selectPage(dept,pageNum, pageSize);
        return Result.success(pageInfo);
    }
}
