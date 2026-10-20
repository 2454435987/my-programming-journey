import request from './request'

// 送水工登录
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取送水工当前个人信息及所在片区
export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

// 获取送水工片区待配任务列表 (手机端工作台专用)
export function getPendingTasks(courierId) {
  return request({
    url: `/delivery/pending-tasks?courierId=${courierId}`,
    method: 'get'
  })
}

// 获取送水工个人历史配送记录
export function getMyDeliveryTasks(params) {
  return request({
    url: '/delivery/history/page',
    method: 'post',
    data: {
      page: params.pageNum || 1,
      size: params.pageSize || 50,
      courierId: params.courierId
    }
  })
}

// 快速按手机号/姓名检索客户
export function searchCustomer(params) {
  return request({
    url: '/customer/search',
    method: 'get',
    params
  })
}

// 获取客户水票余额
export function getCustomerTickets(customerId) {
  return request({
    url: `/customer/${customerId}/tickets`,
    method: 'get'
  })
}

// AI 空桶质检分析接口 (支持多模态演示样本或拍照上传)
export function inspectBarrel(formData) {
  return request({
    url: '/delivery/barrel/ai-inspect',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}

// 现场一键核销水票并记录空桶回收
export function verifyAndDeliver(data) {
  return request({
    url: '/delivery/verify-and-deliver',
    method: 'post',
    data: {
      customerId: data.customerId,
      courierId: data.courierId,
      typeId: data.typeId || data.ticketTypeId,
      deliveryQty: data.deliveryQty || 1,
      recycleQty: data.recycleQty || 0,
      aiInspectStatus: data.aiInspectStatus || 'PASSED',
      aiInspectDesc: data.aiInspectDesc || '现场质检合格',
      suggestDeductDeposit: data.suggestDeductDeposit || 0,
      inspectImageUrl: data.inspectImageUrl || '',
      signMethod: '送水工现场扫码核销'
    }
  })
}

// 修改密码
export function updatePassword(data) {
  return request({
    url: '/auth/password',
    method: 'put',
    data
  })
}
