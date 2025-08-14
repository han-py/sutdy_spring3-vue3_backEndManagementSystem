import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'manager', meta: { title: '管理员主页' }, component: () => import('../views/manger.vue') },
    { path:'/404', name: 'notFound', meta: { title: '页面未找到' }, component: () => import('../views/404.vue') },
    { path: '/:pathMatch(.*)*', redirect: '/404'}
  ],
})

export default router
