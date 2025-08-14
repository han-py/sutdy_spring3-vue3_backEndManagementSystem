<template>
  <div style="text-align: center">
    <img src="@/assets/404.png" alt="">
    <div><el-button type="primary" style="width: 150px; height: 60px; font-size: 20px" @click="router.push('/manager')">返回主页</el-button></div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
</script>