import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/manager'},
    { path: '/manager', name: 'manger', meta: { title: '管理员主菜单' }, component: () => import('../views/manager.vue') },
    { path: "/404", name: "404", meta: { title: "页面不存在" }, component: () => import("../views/404.vue")},
    { path: '/:pathMatch(.*)*', redirect: '/404'}
  ],
})

export default router
