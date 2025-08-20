<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding: 50px 30px; background-color: white; margin-left: 100px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.2)">
        <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 300px">
          <div style="margin-bottom: 30px; font-size: 24px; text-align: center; color: #0742b1; font-weight: bold">欢迎登录后台管理系统</div>
          <el-form-item prop="username">
            <el-input size="large" v-model="data.form.username" placeholder="请输入账号" prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input show-password size="large" v-model="data.form.password" placeholder="请输入密码" prefix-icon="Lock"></el-input>
          </el-form-item>
          <div style="margin-bottom: 20px">
            <el-button @click="login" size="large" style="width: 100%" type="primary">登 录</el-button>
          </div>
          <div style="text-align: right">还没有账号？请 <a style="color: #0742b1; text-decoration: none" href="/register">注册</a></div>
        </el-form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  form: {},
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  }
})
const formRef = ref()

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {  // 登录成功
          // 存储后台返回的用户数据信息
          localStorage.setItem('xm-pro-user', JSON.stringify(res.data))  // 把json对象转换成 json字符串存储
          ElMessage.success('登录成功')
          setTimeout(() => {
            location.href = '/manager/home'
          }, 500)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow:hidden;
  background:url("@/assets/bg.jpg");
  background-size: cover;
  background-position: 0px 0px;
}

.login-box{
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  right: 0;
  position: absolute;
}
</style>