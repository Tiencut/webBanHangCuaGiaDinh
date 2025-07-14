import api from './config.js';

export const ordersApi = {
  // Lấy danh sách đơn hàng với phân trang
  getAll: (page = 0, size = 10, search = '', status = null, customerId = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (status) params.append('status', status);
    if (customerId) params.append('customerId', customerId);
    
    return api.get(`/orders?${params.toString()}`);
  },

  // Lấy chi tiết đơn hàng theo ID
  getOrderById: (id) => {
    return api.get(`/orders/${id}`);
  },

  // Tạo đơn hàng mới
  create: (orderData) => {
    return api.post('/orders', orderData);
  },

  // Cập nhật đơn hàng
  update: (id, orderData) => {
    return api.put(`/orders/${id}`, orderData);
  },

  // Xóa đơn hàng
  delete: (id) => {
    return api.delete(`/orders/${id}`);
  },

  // Cập nhật trạng thái đơn hàng
  updateOrderStatus: (id, status) => {
    return api.patch(`/orders/${id}/status`, { status });
  },

  // Lấy thống kê đơn hàng
  getOrderStats: () => {
    return api.get('/orders/stats');
  }
}; 