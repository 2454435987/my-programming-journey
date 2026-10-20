import axios from 'axios'
import { showToast } from 'vant'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('courier_token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      showToast({ message: res.message || '操作失败', type: 'fail' })
      if (res.code === 401) {
        localStorage.removeItem('courier_token')
        localStorage.removeItem('courier_info')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data
  },
  error => {
    const msg = error.response?.data?.message || error.message || '网络连接异常'
    showToast({ message: msg, type: 'fail' })
    if (error.response?.status === 401) {
      localStorage.removeItem('courier_token')
      localStorage.removeItem('courier_info')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default service
