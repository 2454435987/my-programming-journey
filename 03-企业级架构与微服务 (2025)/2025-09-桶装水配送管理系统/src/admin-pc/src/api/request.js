import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('water_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    // If business code is not 200, show error
    if (res.code !== 200) {
      ElMessage.error(res.message || '业务操作失败')
      if (res.code === 401) {
        localStorage.removeItem('water_token')
        localStorage.removeItem('water_user')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data
  },
  error => {
    const msg = error.response?.data?.message || error.message || '网络连接异常，请重试'
    ElMessage.error(msg)
    if (error.response?.status === 401) {
      localStorage.removeItem('water_token')
      localStorage.removeItem('water_user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default service
