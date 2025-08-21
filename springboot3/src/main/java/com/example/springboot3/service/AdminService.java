package com.example.springboot3.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot3.entity.Admin;
import com.example.springboot3.exception.CustomException;
import com.example.springboot3.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    public List<Admin> selectAll(Admin  admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public PageInfo<Admin> selectPage(Admin admin,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public void add(Admin admin) {
        String username = admin.getUsername();  // 账号
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) {  // 注册的账号已存在  无法注册  注意：管理员账号无法通过注册的方式新增
            throw new CustomException("500", "账号已存在，请更换别的账号");
        }
        if (StrUtil.isBlank(admin.getPassword())) {  // 密码没填
            admin.setPassword("admin");  // 默认密码 admin
        }
        if (StrUtil.isBlank(admin.getName())) {  // 名字没填
            admin.setName(admin.getUsername());  // 默认名称
        }
        // 一定要设置角色
        admin.setRole("EMP");  // 员工的角色
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void delete(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public Admin login(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {
            throw new CustomException("500", "账号不存在");
        }
        String password = admin.getPassword();
        if (!password.equals(dbAdmin.getPassword())) {
            throw new CustomException("500", "账号或密码错误");
        }
        return dbAdmin;
    }
}
