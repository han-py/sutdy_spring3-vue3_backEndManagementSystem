# sutdy_spring3-vue3_backEndManagementSystem
## 用于初步学习前后端分离 后台管理系统
***
### 初始化
#### 大纲 https://javaxm.cn/free/1txhsv.html
#### B站视频链接 https://www.bilibili.com/video/BV1Df2cYVEWo/?spm_id_from=333.1387.favlist.content.click&vd_source=fb913aea8f391cf63bd3a7bee690795c
#### 绿叶学习网 http://www.lvyestudy.com/
#### 菜鸟教程 https://www.runoob.com/
#### markdown语法 https://markdown.com.cn/intro.html
#### 图片取色器 https://www.jyshare.com/front-end/6214/#65ae8f
#### 找logo就上 https://www.iconfont.cn/
# !!!注意：在终端进行操作时要注意是不是在对应的文件夹，如不是，及时用cd命令进入对应的文件夹。
***
#### 配置git代理
```
git config --global http.proxy http://127.0.0.1:端口号
git config --global https.proxy http://127.0.0.1:端口号
```
***
### 技术栈
#### 前后端分离
#### 后端：SpringBoot3 + Mybatis
#### 前端：Vue3 + Element-Plus
#### 数据库：Mysql
***
### 编程软件
#### 必备软件：JDK17 以上版本、IDEA2023 以上版本（旗舰版）、Mysql5.7（不能低于5.7，5.6到5.7有很大变化） 或 8.0、nodejs16 以上版本、 Navicat（数据库可视化工具）
***
### 笔记
#### 构建 vue 工程时只有 Router 选“是”其他都不要，可以保留示例文件以更快更方便的初始化
#### Element-Plus 国内镜像网址 https://cn.element-plus.org/zh-CN/
***
#### 安装 Element-Plus组件: 
```
cd vue3
npm i element-plus -s
```
***
#### Element-Plus 主题色设置
##### 安装依赖
```
npm i sass@1.71.1 -D
npm i unplugin-auto-import -D
npm i unplugin-element-plus -D
npm i unplugin-vue-components -D
```
#### 配置 index.scss
```
@forward "element-plus/theme-chalk/src/common/var.scss" with ($colors: (
  "primary": ("base": #0742b1),
  "success": ("base": #2b8f01),
  "warning": ("base": #ffad00),
  "danger": ("base": #e52f2f),
  "info": ("base": #5e41b8),
));
```
#### 配置 vite.config.js
```
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import ElementPlus from 'unplugin-element-plus/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // 按需定制主题配置
    ElementPlus({
      useSource: true,
    }),
    AutoImport({
      resolvers: [ElementPlusResolver({ importStyle: 'sass' })],
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: 'sass' })],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },

  css: {
    preprocessorOptions: {
      scss: {
        // 自动导入定制化样式文件进行样式覆盖
        additionalData: `
        @use "@/assets/index.scss" as *;
      `,
      }
    }
  },
})
```
***
### Result 类公式：
```
/**
 * 统一返回的包装类
 */
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("请求成功");
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode("500");
        result.setMsg("系统错误");
        return result;
    }

    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```
***
### Springboot 配置 Mybatis （记得要在 resource 下再创建一个 mapper 文件夹）
```
# 配置mybatis实体和xml映射
mybatis:
  ## 映射xml
  mapper-locations: classpath:mapper/*.xml
  configuration:
    # 配置日志
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
```
***
### 分页查询
#### 引入 pagehelper 插件
```
<!-- 分页插件pagehelper -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.4.6</version>
    <exclusions>
        <exclusion>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
#### 三行代码实现分页查询
```
public PageInfo<Employee> selectPage(Integer pageNum, Integer pageSize) {
    PageHelper.startPage(pageNum, pageSize);
    List<Employee> list = employeeMapper.selectAll();
    return PageInfo.of(list);
}
```
#### 分页接口
```
/**
 * 分页查询的数据
 * pageNum: 当前页码
 * pageSize: 每页的个数
 */
@GetMapping("/selectPage")
public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize) {
    PageInfo<Employee> pageInfo = employeeService.selectPage(pageNum, pageSize);
    return Result.success(pageInfo);
}
```
***
### 安装 axios 封装前后端对接数据工具
```
npm i axios -S
```
#### 请求的工具类 request.js
```
import axios from "axios";
import {ElMessage} from "element-plus";

const request = axios.create({
  baseURL: 'http://localhost:9090',
  timeout: 30000  // 后台接口超时时间
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=utf-8';
  return config
}, error => {
  return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
  response => {
    let res = response.data;
    // 兼容服务端返回的字符串数据
    if (typeof res === 'string') {
      res = res ? JSON.parse(res) : res
    }
    return res;
  },
  error => {
    if (error.response.status === 404) {
      ElMessage.error('未找到请求接口')
    } else if (error.response.status === 500) {
      ElMessage.error('系统异常，请查看后端控制台报错')
    } else {
      console.error(error.message)
    }
    return Promise.reject(error)
  }
)

export default request
```
***
### 在 Springboot 里面设置统一的跨域处理
```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1 设置访问源地址
        corsConfiguration.addAllowedHeader("*"); // 2 设置访问源请求头
        corsConfiguration.addAllowedMethod("*"); // 3 设置访问源请求方法
        source.registerCorsConfiguration("/**", corsConfiguration); // 4 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
```
***
### 注意：MySQL中如果表名是关键字（如employee），需要用反引号(`)而不是单引号(')包围。
### 注意：前后端的接口名称要对应，尤其注意后端接口最后该不该有(/)。
***
### 构造函数注入方式（推荐）
#### 使用构造函数注入替代字段注入：
```
   @RestController
   public class WebController {
       
       private final EmployeeService employeeService;
       
       public WebController(EmployeeService employeeService) {
           this.employeeService = employeeService;
       }
       
       // 其他方法
   }
```
***
### hutool 工具包 https://doc.hutool.cn/pages/index/
```
<!--  hutool 插件包 -->
<dependency>
  <groupId>cn.hutool</groupId>
  <artifactId>hutool-all</artifactId>
  <version>5.8.25</version>
</dependency>
```
***
### 快速替换：选中要替换的部分，然后按快捷键 Ctrl + R
***
### SpringBoot3+Vue3实现文件上传下载功能
#### 文件上传
##### // System.getProperty("user.dir") 获取到你当前这个项目的根路径
##### // 文件上传的目录的路径
```
private static final String filePath = System.getProperty("user.dir") + "/files/";
```
```
/**
 * 文件上传
 */
@PostMapping("/upload")
public Result upload(MultipartFile file) {   // 文件流的形式接收前端发送过来的文件
    String originalFilename = file.getOriginalFilename();  // xxx.png
    if (!FileUtil.isDirectory(filePath)) {  // 如果目录不存在 需要先创建目录
        FileUtil.mkdir(filePath);  // 创建一个 files 目录
    }
    // 提供文件存储的完整的路径
    // 给文件名 加一个唯一的标识
    String fileName = System.currentTimeMillis() + "_" + originalFilename;  // 156723232322_xxx.png
    String realPath = filePath + fileName;   // 完整的文件路径
    try {
        FileUtil.writeBytes(file.getBytes(), realPath);
    } catch (IOException e) {
        e.printStackTrace();
        throw new CustomException("500", "文件上传失败");
    }
    // 返回一个网络连接
    // http://localhost:9090/files/download/xxxx.jpg
    String url = "http://localhost:9090/files/download/" + fileName;
    return Result.success(url);
}
```
#### 文件下载
```
response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
response.setContentType("application/octet-stream");
```
```
/**
 * 文件下载
 */
@GetMapping("/download/{fileName}")
public void download(@PathVariable String fileName, HttpServletResponse response) {
    try {
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");
        OutputStream os = response.getOutputStream();
        String realPath = filePath + fileName;   // 完整的文件路径：http://localhost:9090/files/download/1729672708145_123.jpg
        // 获取到文件的字节数组
        byte[] bytes = FileUtil.readBytes(realPath);
        os.write(bytes);
        os.flush();
        os.close();
    } catch (IOException e) {
        e.printStackTrace();
        throw new CustomException("500", "文件下载失败");
    }
}
```
#### 前端对接文件上传和下载
##### 用户头像
##### upload 组件
```
<div style="width: 100%; display: flex; justify-content: center; margin-bottom: 20px">
  <el-upload
      class="avatar-uploader"
      action="http://localhost:9090/files/upload"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
  >
    <img v-if="data.form.avatar" :src="data.form.avatar" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
  </el-upload>
</div>
```
##### 回调函数
```
const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data
}
```
#### 表格里面显示图片
```
<el-table-column label="头像">
  <template #default="scope">
    <img v-if="scope.row.avatar" :src="scope.row.avatar" alt="" style="display: block; width: 40px; height: 40px; border-radius: 50%" />
  </template>
</el-table-column>
```
#### 上传头像
```
<el-form-item label="头像">
  <el-upload
      action="http://localhost:9090/files/upload"
      list-type="picture"
      :on-success="handleAvatarSuccess"
  >
    <el-button type="primary">上传头像</el-button>
  </el-upload>
</el-form-item>

const handleAvatarSuccess = (res) => {
  data.form.avatar = res.data
}
```
***
