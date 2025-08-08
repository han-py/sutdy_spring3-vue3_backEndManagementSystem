import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import '@/assets/global.css'
import {zhCn} from "element-plus/es/locale/index";

const app = createApp(App)

app.use(router)

//element-plus 语言包，变成中文
app.use(ElementPlus, {
    locale: zhCn,
})

app.mount('#app')
