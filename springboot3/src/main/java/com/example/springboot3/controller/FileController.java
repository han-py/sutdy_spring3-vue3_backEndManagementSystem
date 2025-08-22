package com.example.springboot3.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.example.springboot3.common.Result;
import com.example.springboot3.exception.CustomException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文件相关的接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private static final String filePath = System.getProperty("user.dir") + "/files/";

    /**
     * 文件上传接口
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile file) { //文件流的形式接受前端发送过来的文件

        String originalFilename = file.getOriginalFilename(); //获取文件的原始名称
        if(!FileUtil.isDirectory(filePath)){ // 如果目录不存在，需要先创建目录
            FileUtil.mkdir(filePath); // 创建一个files目录
        }

        // 提供文件完整的目录
        // 给文件名加一个唯一的标识
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName; // 文件的完整路径
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }

        // 返回一个网络链接
        String url = "http://localhost:9090/files/download/" + fileName; // 访问这个链接可以下载这个文件
        return Result.success(url);
    }

    /**
     * 文件下载接口
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse  response) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");

            OutputStream os = response.getOutputStream();
            String realPath = filePath + fileName; // 文件的完整路径
            // 获取到文件的字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write( bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new CustomException("500", "文件下载失败");
        }
    }
}
