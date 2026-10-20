import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue')
  },
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/dashboard/Dashboard.vue'), meta: { title: '数据统计' } },
      { path: 'users', name: 'Users', component: () => import('@/views/user/UserList.vue'), meta: { title: '用户管理' } },
      { path: 'content/knowledge', name: 'Knowledge', component: () => import('@/views/content/KnowledgeList.vue'), meta: { title: '知识管理' } },
      { path: 'content/courses', name: 'Courses', component: () => import('@/views/content/CourseList.vue'), meta: { title: '教程管理' } },
      { path: 'content/categories', name: 'Categories', component: () => import('@/views/content/CategoryList.vue'), meta: { title: '分类管理' } },
      { path: 'mall/products', name: 'Products', component: () => import('@/views/mall/ProductList.vue'), meta: { title: '商品管理' } },
      { path: 'mall/orders', name: 'Orders', component: () => import('@/views/mall/OrderList.vue'), meta: { title: '订单管理' } },
      { path: 'community/posts', name: 'Posts', component: () => import('@/views/community/PostList.vue'), meta: { title: '帖子管理' } },
      { path: 'community/comments', name: 'Comments', component: () => import('@/views/community/CommentList.vue'), meta: { title: '评论管理' } },
      { path: 'activities', name: 'Activities', component: () => import('@/views/activity/ActivityList.vue'), meta: { title: '活动管理' } },
      { path: 'operation/banners', name: 'Banners', component: () => import('@/views/operation/BannerList.vue'), meta: { title: 'Banner管理' } },
      { path: 'operation/messages', name: 'Messages', component: () => import('@/views/operation/MessageList.vue'), meta: { title: '消息通知' } },
      { path: 'system/admins', name: 'Admins', component: () => import('@/views/system/AdminList.vue'), meta: { title: '管理员管理' } },
      { path: 'system/roles', name: 'Roles', component: () => import('@/views/system/RoleList.vue'), meta: { title: '角色管理' } },
      { path: 'system/menus', name: 'Menus', component: () => import('@/views/system/MenuList.vue'), meta: { title: '菜单管理' } },
      { path: 'system/logs', name: 'Logs', component: () => import('@/views/system/LogList.vue'), meta: { title: '操作日志' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path !== '/login' && !getToken()) {
    next('/login')
  } else {
    next()
  }
})

export default router
