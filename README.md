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
### SpringBoot3+Vue3实现富文本编辑器功能
#### wangeditor5官网 https://www.wangeditor.com/
### 安装wangeditor5
```
npm install @wangeditor/editor --save
npm install @wangeditor/editor-for-vue@next --save
```
### 引入和使用 wangeditor5
```
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef} from "vue";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
```
##### 初始化（表单中）
##### wangEditor 5 富文本字段可以直接和form中的字段使用v-model进行绑定
```
<div style="border: 1px solid #ccc; width: 100%">
  <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :mode="mode"
  />
  <Editor
      style="height: 500px; overflow-y: hidden;"
      v-model="data.form.content"
      :mode="mode"
      :defaultConfig="editorConfig"
      @onCreated="handleCreated"
  />
</div>
```
```
/* wangEditor5 初始化开始 */
const baseUrl = 'http://localhost:9090'
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default' 
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor 
}
/* wangEditor5 初始化结束 */
```
### 在表格里查看富文本内容
```
<el-table-column label="内容">
  <template #default="scope">
    <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
  </template>
</el-table-column>

<el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
  <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.viewVisible = false">关 闭</el-button>
    </span>
  </template>
</el-dialog>             

const view = (content) => {
  data.content = content
  data.viewVisible = true
}

data: {
  viewVisible: false,
  content: null    
}
```
### 富文本对应的后端文件上传接口
```
/**
 * wang-editor编辑器文件上传接口
 */
@PostMapping("/wang/upload")
public Map<String, Object> wangEditorUpload(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();  
    if (!FileUtil.isDirectory(filePath)) {  
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
    String url = "http://localhost:9090/files/download/" + fileName;
   // wangEditor上传图片成功后， 需要返回的参数
    Map<String, Object> resMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();
    Map<String, Object> urlMap = new HashMap<>();
    urlMap.put("url", url);
    list.add(urlMap);
    resMap.put("errno", 0);
    resMap.put("data", list);
    return resMap;
}
```
### 外部引入 view.css （可选）
```
.editor-content-view {
    padding: 0 10px;
    margin-top: 20px;
    overflow-x: auto;
}

.editor-content-view p,
.editor-content-view li {
    white-space: pre-wrap; /* 保留空格 */
}

.editor-content-view blockquote {
    border-left: 8px solid #d0e5f2;
    padding: 10px 10px;
    margin: 10px 0;
    background-color: #f1f1f1;
}

.editor-content-view code {
    font-family: monospace;
    background-color: #eee;
    padding: 3px;
    border-radius: 3px;
}
.editor-content-view pre>code {
    display: block;
    padding: 10px;
}

.editor-content-view table {
    border-collapse: collapse;
}
.editor-content-view td,
.editor-content-view th {
    border: 1px solid #ccc;
    min-width: 50px;
    height: 20px;
}
.editor-content-view th {
    background-color: #f1f1f1;
}

.editor-content-view ul,
.editor-content-view ol {
    padding-left: 20px;
}

.editor-content-view input[type="checkbox"] {
    margin-right: 5px;
}
```
***
### 数据批量导入导出功能（通过 excel 导入导出）
#### 引入 poi-ooxml 依赖
```
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.3.0</version>
</dependency>
```
#### 后端代码
```
/**
     * 导出excel
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 1. 拿到所有的员工数据
        List<Employee> employeeList = employeeService.selectAll(null);
        // 2. 构建 ExcelWriter
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("sex", "性别");
        writer.addHeaderAlias("no", "工号");
        writer.addHeaderAlias("age", "年龄");
        writer.addHeaderAlias("description", "个人介绍");
        writer.addHeaderAlias("departmentName", "部门");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(employeeList, true);
        // 5. 设置输出的文件的名称  以及输出流的头信息
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    /**
     * excel 导入
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        // 1. 拿到输入流 构建 reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 2. 读取 excel里面的数据
        reader.addHeaderAlias("账号", "username");
        reader.addHeaderAlias("名称", "name");
        reader.addHeaderAlias("性别", "sex");
        reader.addHeaderAlias("工号", "no");
        reader.addHeaderAlias("年龄", "age");
        reader.addHeaderAlias("个人介绍", "description");
        reader.addHeaderAlias( "部门", "departmentName");
        List<Employee> employeeList = reader.readAll(Employee.class);
        // 3. 写入list数据到数据库
        for (Employee employee : employeeList) {
            employeeService.add(employee);
        }
        return Result.success();
    }
```
#### 前端代码
```
<el-upload
          style="display: inline-block; margin: 0 10px"
          action="http://localhost:9090/employee/import"
          :show-file-list="false"
          :on-success="importSuccess"
      >
        <el-button type="info">导入</el-button>
      </el-upload>
      <el-button type="success" @click="exportData">导出</el-button>
```
```
const exportData = () => {
  // 导出数据 是通过流的形式下载 excel   打开流的链接，浏览器会自动帮我们下载文件
  window.open('http://localhost:9090/employee/export')
}
```
```
const importSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入数据成功')
    load()
  } else {
    ElMessage.error(res.msg)
  }
}
```
***
### SpringBoot3+Vue3实现数据统计图表功能
#### Ecahrts 官网 https://echarts.apache.org/zh/index.html
### 安装使用 Echarts
```
npm i echarts -S
```
##### dom 容器
```
<div style="width=600px; height=400px" id="main"></div>
```
```
import * as echarts from 'echarts';

const option = {
  title: {
    text: 'ECharts 入门示例'
  },
  tooltip: {},
  legend: {
    data: ['销量']
  },
  xAxis: {
    data: ['衬衫', '羊毛衫', '雪纺衫', '裤子', '高跟鞋', '袜子']
  },
  yAxis: {},
  series: [
    {
      name: '销量',
      type: 'bar',
      data: [5, 20, 36, 10, 10, 20]
    }
  ]
};
  
// 基于准备好的dom，初始化echarts实例
const myChart = echarts.init(document.getElementById('main'))
  // 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);
```
#### 注意：必须在 dom 准备好的时候再去设置初始化 ecahrts 示例
##### 引入 onMouted
```
import { reactive, onMounted } from "vue";

  
// onMounted 表示页面的所有dom元素都初始化完成了
onMounted(() => {
  // 基于准备好的dom，初始化echarts实例
  const myChart = echarts.init(document.getElementById('main'))
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
})
```
#### 柱状图
```
@GetMapping("/barData")
public Result getBarData() {
    Map<String, Object> map = new HashMap<>();
    List<Employee> employeeList = employeeService.selectAll(null);
    Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
    map.put("department", departmentNameSet);  // x轴数据
    List<Long> countList = new ArrayList<>();
    for (String departmentName : departmentNameSet) {
        // 统计这个部门下面的员工的数量
        long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
        countList.add(count);
    }
    map.put("count", countList);  // y轴员工数量的数据
    return Result.success(map);
}
```
##### 设置颜色
```
itemStyle: {
    normal: {
      color: function () {
        return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
      }
    },
},
```
### 折线图
```
@GetMapping("/lineData")
public Result getLineData() {
    Map<String, Object> map = new HashMap<>();

    Date date = new Date();  // 今天当前的时间
    DateTime start = DateUtil.offsetDay(date, -7); // 起始日期
    List<DateTime> dateTimeList = DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
    // 把 DateTime 类型的日期转换成  字符串类型的日期  ["10月22日", "10月23日"...]
    List<String> dateStrList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
            .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
    map.put("date", dateStrList);  // x轴数据

    List<Integer> countList = new ArrayList<>();
    for (DateTime day : dateTimeList) {
        // 10月22日
        String dayFormat = DateUtil.formatDate(day);  // 2023-10-22
        // 获取当天所有的发布的文章的数量
        Integer count = articleService.selectCountByDate(dayFormat);
        countList.add(count);
    }
    map.put("count", countList);  // y轴发布文章的数量数据
    return Result.success(map);
}
```
### 饼图
```
@GetMapping("/pieData")
public Result getPieData() {
    List<Map<String, Object>> list = new ArrayList<>();
    List<Employee> employeeList = employeeService.selectAll(null);
    Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
    for (String departmentName : departmentNameSet) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", departmentName);
        // 统计这个部门下面的员工的数量
        long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
        map.put("value", count);
        list.add(map);
    }
    return Result.success(list);
}
```
***
# 完结撒花！！！
### 花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花花
***
