  // Lấy công nợ khách hàng
  getDebt: (customerId) => {
    return api.get(`/customers/${customerId}/debt`);
  },
import api from './config.js';

export const customersApi = {
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