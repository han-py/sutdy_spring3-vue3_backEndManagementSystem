import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login'},
    { path: '/manager', name: 'manager', meta: { title: '管理员主菜单' }, component: () => import('../views/Manager.vue'), children: [
        { path: 'data', name: 'user', meta: { title: '数据展示页面' }, component: () => import('../views/Data.vue')},
        { path: 'employee', name: 'employee', meta: { title: '员工信息页面' }, component: () => import('../views/Employee.vue')},
        { path: 'admin', name: 'admin', meta: { title: '管理员信息页面' }, component: () => import('../views/Admin.vue')},
        { path: 'person', name: 'person', meta: { title: '个人信息页面' }, component: () => import('../views/Person.vue')},
        { path: "404", name: "404", meta: { title: "页面不存在" }, component: () => import("../views/404.vue")},
        { path: ':pathMatch(.*)*', redirect: '/404'},
      ] },
    { path: '/login', name: 'login', meta: { title: '登录页面' }, component: () => import('../views/Login.vue')},
    { path: '/register', name: 'register', meta: { title: '注册页面' }, component: () => import('../views/Register.vue')},
    { path: "/404", name: "404", meta: { title: "页面不存在" }, component: () => import("../views/404.vue")},
    { path: '/:pathMatch(.*)*', redirect: '/404'}
  ],
})

// from：将要进行跳转的当前route对象 (跳转前的一些操作)
// to: 跳转后route对象 (跳转后的一些操作)
// next(): 调用该方法后, 才能进入下一个钩子
router.beforeEach((to, from, next) => {
  document.title = to.meta.title;
  next()
})

export default router
