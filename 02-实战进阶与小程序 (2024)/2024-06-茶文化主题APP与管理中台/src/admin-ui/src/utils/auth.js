export function getToken() {
  return localStorage.getItem('admin_token')
}
export function setToken(token) {
  localStorage.setItem('admin_token', token)
}
export function removeToken() {
  localStorage.removeItem('admin_token')
}
export function getAdminInfo() {
  try {
    return JSON.parse(localStorage.getItem('admin_info') || '{}')
  } catch {
    return {}
  }
}
export function setAdminInfo(info) {
  localStorage.setItem('admin_info', JSON.stringify(info))
}
export function removeAdminInfo() {
  localStorage.removeItem('admin_info')
}
