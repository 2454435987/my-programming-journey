import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/Index.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Index.vue'),
        meta: { title: '运营看板', icon: 'DataAnalysis' }
      },
      {
        path: 'couriers',
        name: 'Couriers',
        component: () => import('@/views/couriers/Index.vue'),
        meta: { title: '送水工与片区', icon: 'Van' }
      },
      {
        path: 'customers',
        name: 'Customers',
        component: () => import('@/views/customers/Index.vue'),
        meta: { title: '客户档案与水票', icon: 'UserFilled' }
      },
      {
        path: 'ticket-flow',
        name: 'TicketFlow',
        component: () => import('@/views/ticket/Flow.vue'),
        meta: { title: '水票审计流水', icon: 'Tickets' }
      },
      {
        path: 'history',
        name: 'DeliveryHistory',
        component: () => import('@/views/history/Index.vue'),
        meta: { title: '配送核销记录', icon: 'List' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('water_token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    document.title = (to.meta.title ? `${to.meta.title} - ` : '') + '桶装水配送管理系统'
    next()
  }
})

export default router
