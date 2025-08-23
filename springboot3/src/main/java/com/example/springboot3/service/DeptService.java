package com.example.springboot3.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot3.entity.Account;
import com.example.springboot3.entity.Dept;
import com.example.springboot3.exception.CustomException;
import com.example.springboot3.mapper.DeptMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptService {

    @Resource
    DeptMapper deptMapper;

    public List<Dept> selectAll(Dept  dept) {
        return deptMapper.selectAll(dept);
    }

    public Dept selectById(Integer id) {
        return deptMapper.selectById(id);
    }

    public PageInfo<Dept> selectPage(Dept dept,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Dept> list = deptMapper.selectAll(dept);
        return PageInfo.of(list);
    }

    public void add(Dept dept) {
        deptMapper.insert(dept);
    }

    public void update(Dept dept) {
        deptMapper.updateById(dept);
    }

    public void delete(Integer id) {
        deptMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }
}
