package com.example.springboot3.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot3.common.Result;
import com.example.springboot3.entity.Account;
import com.example.springboot3.entity.Employee;
import com.example.springboot3.exception.CustomException;
import com.example.springboot3.service.AdminService;
import com.example.springboot3.service.ArticleService;
import com.example.springboot3.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {
    @Resource
    private EmployeeService employeeService;

    @Resource
    private AdminService adminService;

    @Resource
    private ArticleService articleService;

    public WebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/weather")
    public Result weather() {
        return Result.success("今天天气：晴 23摄氏度");
    }

    @GetMapping("/count")
    public Result count() {
        throw new CustomException("400", "错误！禁止请求");
//        throw new RuntimeException("错误！禁止请求");
    }

    @GetMapping("/map")
    public Result map() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "HaPpY");
        map.put("age", 20);
        return Result.success(map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if("ADMIN".equals(account.getRole())){
            result = adminService.login( account);
        } else if ("EMP".equals(account.getRole())) {
            result = employeeService.login(account);
        } else {
            throw new CustomException("500", "非法输入");
        }
        return Result.success(result);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {

        employeeService.add(employee);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {  // 管理员登录
            adminService.updatePassword(account);
        } else if ("EMP".equals(account.getRole())) {
            employeeService.updatePassword(account);
        } else {
            throw new CustomException("500", "非法输入");
        }
        return Result.success();
    }

    @GetMapping("/barData")
    public Result getBarData() {
        Map<String, Object> map = new HashMap<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDeptName).collect(Collectors.toSet());
        map.put("department", departmentNameSet);  // x轴数据
        List<Long> countList = new ArrayList<>();
        for (String departmentName : departmentNameSet) {
            // 统计这个部门下面的员工的数量
            long count = employeeList.stream().filter(employee -> employee.getDeptName().equals(departmentName)).count();
            countList.add(count);
        }
        map.put("count", countList);  // y轴员工数量的数据
        return Result.success(map);
    }

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

    @GetMapping("/pieData")
    public Result getPieData() {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Employee> employeeList = employeeService.selectAll(null);
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDeptName).collect(Collectors.toSet());
        for (String departmentName : departmentNameSet) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", departmentName);
            // 统计这个部门下面的员工的数量
            long count = employeeList.stream().filter(employee -> employee.getDeptName().equals(departmentName)).count();
            map.put("value", count);
            list.add(map);
        }
        return Result.success(list);
    }
}
