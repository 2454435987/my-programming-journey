import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Index.vue'),
    meta: { title: '送水工登录' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/Index.vue'),
    redirect: '/tasks',
    children: [
      {
        path: 'tasks',
        name: 'Tasks',
        component: () => import('@/views/tasks/Index.vue'),
        meta: { title: '待配送任务' }
      },
      {
        path: 'verify',
        name: 'Verify',
        component: () => import('@/views/verify/Index.vue'),
        meta: { title: '核销与AI质检' }
      },
      {
        path: 'records',
        name: 'Records',
        component: () => import('@/views/records/Index.vue'),
        meta: { title: '我的业绩' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/profile/Index.vue'),
        meta: { title: '个人中心' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('courier_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/tasks')
  } else {
    document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '送水工工作台'
    next()
  }
})

export default router
