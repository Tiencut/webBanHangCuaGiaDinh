
import api from './config.js';

// API lấy báo cáo công nợ chi tiết
// GET /customers/{customerId}/debt-report
// Trả về: { totalDebt, unpaidOrders: [], lastPayment, paymentHistory: [] }

// API lấy lịch sử mua hàng nâng cao (có phân trang, lọc, tổng tiền, trạng thái...)
// GET /customers/{customerId}/orders?fromDate=&toDate=&status=&page=&size=


export const customersApi = {
  // Lấy công nợ khách hàng đơn giản
  getDebt: (customerId) => {
    return api.get(`/customers/${customerId}/debt`);
  },

  // Lấy báo cáo công nợ chi tiết
  getDebtReport: (customerId) => {
    return api.get(`/customers/${customerId}/debt-report`);
  },

  // Lấy lịch sử mua hàng nâng cao (có lọc, phân trang)
  getAdvancedOrders: (customerId, params = {}) => {
    // params: { fromDate, toDate, status, page, size }
    return api.get(`/customers/${customerId}/orders`, { params });
  },

  // Lấy danh sách khách hàng với phân trang
  getAll: (page = 0, size = 10, search = '', type = null, status = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (type) params.append('type', type);
    if (status) params.append('status', status);
    
    return api.get(`/customers?${params.toString()}`);
  },

  // Lấy chi tiết khách hàng theo ID
  getCustomerById: (id) => {
    return api.get(`/customers/${id}`);
  },

  // Tạo khách hàng mới
  create: (customerData) => {
    return api.post('/customers', customerData);
  },

  // Cập nhật khách hàng
  update: (id, customerData) => {
    return api.put(`/customers/${id}`, customerData);
  },

  // Xóa khách hàng
  delete: (id) => {
    return api.delete(`/customers/${id}`);
  },

  // Lấy lịch sử đơn hàng của khách hàng
  getCustomerOrders: (customerId) => {
    return api.get(`/customers/${customerId}/orders`);
  }
}; 