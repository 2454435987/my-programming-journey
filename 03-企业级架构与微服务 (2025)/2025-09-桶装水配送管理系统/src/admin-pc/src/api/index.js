import request from './request'

// 认证相关
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getUserInfo() {
  return request({
    url: '/auth/info',
    method: 'get'
  })
}

export function updatePassword(data) {
  return request({
    url: '/auth/password',
    method: 'put',
    data
  })
}

// 片区管理
export function getRegionList() {
  return request({
    url: '/region/list',
    method: 'get'
  })
}

// 送水工管理
export function getCourierPage(params) {
  return request({
    url: '/courier/page',
    method: 'get',
    params: {
      page: params.pageNum || 1,
      size: params.pageSize || 10,
      keyword: params.keyword,
      regionId: params.regionId,
      status: params.status
    }
  })
}

export function assignCourierRegion(data) {
  const courierId = data.courierId || data.id
  const regionId = data.regionId
  return request({
    url: '/courier/assign-region',
    method: 'put',
    params: { courierId, regionId },
    data: { courierId, regionId }
  })
}

export function updateCourierStatus(data) {
  const courierId = data.courierId || data.id
  const status = data.status
  return request({
    url: '/courier/status',
    method: 'put',
    params: { courierId, status },
    data: { courierId, status }
  })
}

export function getCouriersByRegion(regionId) {
  return request({
    url: `/courier/list-by-region/${regionId}`,
    method: 'get'
  })
}

// 客户管理
export function getCustomerPage(params) {
  return request({
    url: '/customer/page',
    method: 'post',
    data: {
      page: params.pageNum || 1,
      size: params.pageSize || 10,
      keyword: params.keyword,
      regionId: params.regionId,
      onlyWarning: params.onlyWarning || false
    }
  })
}

export function getCustomerTickets(customerId) {
  return request({
    url: `/customer/${customerId}/tickets`,
    method: 'get'
  })
}

export function saveCustomer(data) {
  return request({
    url: '/customer',
    method: 'post',
    data: {
      id: data.id,
      name: data.name || data.customerName,
      phone: data.phone,
      regionId: data.regionId,
      addressDetail: data.addressDetail || data.address,
      floorNum: data.floorNum || data.floorNumber || 1,
      hasElevator: data.hasElevator ?? 1,
      warningThreshold: 2
    }
  })
}

export function searchCustomer(params) {
  return request({
    url: '/customer/search',
    method: 'get',
    params
  })
}

// 水票与台账管理
export function getTicketTypes() {
  return request({
    url: '/ticket/types',
    method: 'get'
  })
}

export function rechargeTicket(data) {
  return request({
    url: '/ticket/recharge',
    method: 'post',
    data: {
      customerId: data.customerId,
      typeId: data.typeId || data.ticketTypeId,
      buyQty: data.buyQty || data.quantity || 1,
      giftQty: data.giftQty || 0,
      payAmount: data.payAmount || data.paidAmount || 0,
      remark: data.remark || ''
    }
  })
}

export function cancelRefundTicket(data) {
  return request({
    url: '/ticket/cancel-refund',
    method: 'post',
    data: {
      customerId: data.customerId,
      typeId: data.typeId || data.ticketTypeId,
      qty: data.qty || data.quantity || 1,
      refundAmount: data.refundAmount || 0,
      reason: data.reason || data.remark || '客户申请退款作废',
      bizType: data.bizType || 4
    }
  })
}

export function getTicketFlowPage(params) {
  return request({
    url: '/ticket/flow/page',
    method: 'get',
    params: {
      page: params.pageNum || 1,
      size: params.pageSize || 10,
      customerId: params.customerId || null,
      keyword: params.keyword || null,
      bizType: params.bizType ?? (params.flowType !== '' ? params.flowType : null),
      startDate: params.startDate || null,
      endDate: params.endDate || null
    }
  })
}

// 配送历史与AI质检
export function getDeliveryHistoryPage(params) {
  return request({
    url: '/delivery/history/page',
    method: 'post',
    data: {
      page: params.pageNum || 1,
      size: params.pageSize || 10,
      keyword: params.customerKeyword || params.keyword || null,
      orderStatus: params.status !== undefined && params.status !== null && params.status !== '' ? params.status : null,
      regionId: params.regionId || null,
      courierId: params.courierId || null,
      startTime: params.startDate ? `${params.startDate} 00:00:00` : null,
      endTime: params.endDate ? `${params.endDate} 23:59:59` : null
    }
  })
}

export function getDeliveryHistorySummary(params) {
  return request({
    url: '/delivery/history/summary',
    method: 'post',
    data: {
      keyword: params.customerKeyword || params.keyword || null,
      orderStatus: params.status !== undefined && params.status !== null && params.status !== '' ? params.status : null,
      regionId: params.regionId || null,
      courierId: params.courierId || null,
      startTime: params.startDate ? `${params.startDate} 00:00:00` : null,
      endTime: params.endDate ? `${params.endDate} 23:59:59` : null
    }
  })
}

export function inspectBarrel(formData) {
  return request({
    url: '/delivery/barrel/ai-inspect',
    method: 'post',
    headers: { 'Content-Type': 'multipart/form-data' },
    data: formData
  })
}

// 统计看板
export function getDashboardMetrics() {
  return request({
    url: '/dashboard/metrics',
    method: 'get'
  })
}
