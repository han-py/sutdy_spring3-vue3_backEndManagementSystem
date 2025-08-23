package com.example.springboot3.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot3.entity.Account;
import com.example.springboot3.entity.Article;
import com.example.springboot3.exception.CustomException;
import com.example.springboot3.mapper.ArticleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    @Resource
    ArticleMapper articleMapper;

    public List<Article> selectAll(Article  article) {
        return articleMapper.selectAll(article);
    }

    public Article selectById(Integer id) {
        return articleMapper.selectById(id);
    }

    public PageInfo<Article> selectPage(Article article,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(article);
        return PageInfo.of(list);
    }

    public void add(Article article) {
        String username = article.getUsername();  // 账号
        Article dbArticle = articleMapper.selectByUsername(username);
        if (dbArticle != null) {  // 注册的账号已存在  无法注册  注意：管理员账号无法通过注册的方式新增
            throw new CustomException("500", "账号已存在，请更换别的账号");
        }
        if (StrUtil.isBlank(article.getPassword())) {  // 密码没填
            article.setPassword("article");  // 默认密码 article
        }
        if (StrUtil.isBlank(article.getName())) {  // 名字没填
            article.setName(article.getUsername());  // 默认名称
        }
        // 一定要设置角色
        article.setRole("ADMIN ");  // 管理员的角色
        articleMapper.insert(article);
    }

    public void update(Article article) {
        articleMapper.updateById(article);
    }

    public void delete(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.delete(id);
        }
    }

    public Article login(Account article) {
        String username = article.getUsername();
        Article dbArticle = articleMapper.selectByUsername(username);
        if (dbArticle == null) {
            throw new CustomException("500", "账号不存在");
        }
        String password = article.getPassword();
        if (!password.equals(dbArticle.getPassword())) {
            throw new CustomException("500", "账号或密码错误");
        }
        return dbArticle;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Article article = this.selectById(id);
        if (!article.getPassword().equals(account.getPassword())) {  // 页面传来的原密码跟数据库密码对比  不匹配就报错
            throw new CustomException("500", "对不起，原密码错误");
        }
        article.setPassword(account.getNewPassword());  // 设置新密码
        this.update(article);
    }
}
