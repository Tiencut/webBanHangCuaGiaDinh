import api from './config'

export const salesApi = {
  // Lấy thống kê bán hàng
  getStats: (period = 'today') => {
    return api.get('/sales/stats', {
      params: { period }
    })
  },

  // Tạo đơn hàng mới
  createOrder: (orderData) => {
    return api.post('/sales/orders', orderData)
  },

  // Lấy danh sách đơn hàng bán
  getOrders: (page = 0, size = 10, status = null, customerId = null) => {
    return api.get('/sales/orders', {
      params: { page, size, status, customerId }
    })
  },

  // Lấy chi tiết đơn hàng
  getOrderById: (orderId) => {
    return api.get(`/sales/orders/${orderId}`)
  },

  // Cập nhật đơn hàng
  updateOrder: (orderId, orderData) => {
    return api.put(`/sales/orders/${orderId}`, orderData)
  },

  // Xóa đơn hàng
  deleteOrder: (orderId) => {
    return api.delete(`/sales/orders/${orderId}`)
  },

  // Cập nhật trạng thái đơn hàng
  updateOrderStatus: (orderId, status) => {
    return api.patch(`/sales/orders/${orderId}/status`, { status })
  },

  // Tạo báo giá
  createQuote: (quoteData) => {
    return api.post('/sales/quotes', quoteData)
  },

  // Lấy danh sách báo giá
  getQuotes: (page = 0, size = 10, status = null) => {
    return api.get('/sales/quotes', {
      params: { page, size, status }
    })
  },

  // Lấy chi tiết báo giá
  getQuoteById: (quoteId) => {
    return api.get(`/sales/quotes/${quoteId}`)
  },

  // Cập nhật báo giá
  updateQuote: (quoteId, quoteData) => {
    return api.put(`/sales/quotes/${quoteId}`, quoteData)
  },

  // Xóa báo giá
  deleteQuote: (quoteId) => {
    return api.delete(`/sales/quotes/${quoteId}`)
  },

  // Chuyển báo giá thành đơn hàng
  convertQuoteToOrder: (quoteId) => {
    return api.post(`/sales/quotes/${quoteId}/convert`)
  },

  // Xử lý thanh toán
  processPayment: (orderId, paymentData) => {
    return api.post(`/sales/orders/${orderId}/payment`, paymentData)
  },

  // Lấy lịch sử thanh toán
  getPaymentHistory: (orderId) => {
    return api.get(`/sales/orders/${orderId}/payments`)
  },

  // Hoàn tiền
  refundPayment: (orderId, refundData) => {
    return api.post(`/sales/orders/${orderId}/refund`, refundData)
  },

  // Lấy sản phẩm bán chạy
  getTopSelling: (period = 'month', limit = 10) => {
    return api.get('/sales/top-selling', {
      params: { period, limit }
    })
  },

  // Lấy doanh thu theo thời gian
  getRevenue: (period = 'month', startDate = null, endDate = null) => {
    return api.get('/sales/revenue', {
      params: { period, startDate, endDate }
    })
  },

  // Export báo cáo bán hàng
  exportReport: (type = 'orders', format = 'excel', filters = {}) => {
    return api.get('/sales/export', {
      params: { type, format, ...filters },
      responseType: 'blob'
    })
  }
} 