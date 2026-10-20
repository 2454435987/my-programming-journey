/** 网络请求封装: 统一 BASE_URL / 错误处理 / 身份头 */
// H5 预览时后端本地端口 19000; mp-weixin 真机可改为部署地址
export const BASE_URL = 'http://127.0.0.1:19000'

export function request(url, method = 'GET', data = {}, header = {}) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        ...header
      },
      success: (res) => {
        const body = res.data
        if (body && body.code === 200) {
          resolve(body.data)
        } else {
          const msg = (body && body.message) || '请求失败'
          uni.showToast({ title: msg, icon: 'none' })
          reject(new Error(msg))
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常,请检查后端服务', icon: 'none' })
        reject(err)
      }
    })
  })
}

export const get = (url, data) => request(url, 'GET', data)
export const post = (url, data) => request(url, 'POST', data)

/** H5 演示身份: URL hash 含 auth=elder|volunteer|admin 时自动登录 */
function h5AutoUser() {
  try {
    // eslint-disable-next-line no-undef
    const hash = (typeof window !== 'undefined' && (window.location.hash || '')) || ''
    const m = hash.match(/auth=(elder|volunteer|admin)/)
    if (!m) return null
    const map = {
      elder: { id: 2, role: 'elder', real_name: '张桂芳', openid: 'wx_openid_elder_001' },
      volunteer: { id: 6, role: 'volunteer', real_name: '陈志强', openid: 'wx_openid_vol_001' },
      admin: { id: 1, role: 'admin', real_name: '马卫国', openid: 'wx_openid_admin_001' }
    }
    const u = map[m[1]]
    if (u) {
      uni.setStorageSync('currentUser', u)
      return u
    }
  } catch (e) {
    /* ignore */
  }
  return null
}

/** 当前登录用户 */
export function currentUser() {
  try {
    const u = uni.getStorageSync('currentUser')
    if (u && u.id) return u
  } catch (e) {
    /* ignore */
  }
  return h5AutoUser()
}

export function setCurrentUser(user) {
  uni.setStorageSync('currentUser', user)
}

export function logout() {
  uni.removeStorageSync('currentUser')
  uni.reLaunch({ url: '/pages/login/login' })
}
