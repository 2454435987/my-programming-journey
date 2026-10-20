/** 通用工具 */
export function roleText(role) {
  return { elder: '老人', volunteer: '志愿者', admin: '管理员' }[role] || role
}

export function statusText(status) {
  return {
    PENDING: '待接单',
    SERVING: '服务中',
    DONE: '已完成',
    CANCELLED: '已取消',
    OPEN: '招募中',
    MATCHED: '已分配',
    CLOSED: '已关闭',
    IN: '服务中',
    OUT: '已签退',
    ACCEPTED: '已通过',
    REJECTED: '未通过',
    RESOLVED: '已解决',
    HANDLING: '处理中'
  }[status] || status
}

export function statusClass(status) {
  return {
    PENDING: 'st-warn',
    SERVING: 'st-primary',
    DONE: 'st-ok',
    CANCELLED: 'st-gray',
    OPEN: 'st-primary',
    MATCHED: 'st-ok',
    REJECTED: 'st-gray',
    RESOLVED: 'st-ok',
    ACCEPTED: 'st-ok'
  }[status] || 'st-gray'
}

export function fmtTime(v) {
  if (!v) return '-'
  return String(v).replace('T', ' ').substring(0, 16)
}
