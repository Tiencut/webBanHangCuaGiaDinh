import api from './config.js';

export const purchaseOrdersApi = {
  // Lấy danh sách đơn đặt hàng
  getAll: (page = 0, size = 10, search = '', status = null, supplierId = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (status) params.append('status', status);
    if (supplierId) params.append('supplierId', supplierId);
    return api.get(`/purchase-orders?${params.toString()}`);
  },

  // Lấy chi tiết đơn đặt hàng
  getById: (id) => api.get(`/purchase-orders/${id}`),

  // Tạo mới đơn đặt hàng
  create: (data) => api.post('/purchase-orders', data),

  // Cập nhật đơn đặt hàng
  update: (id, data) => api.put(`/purchase-orders/${id}`, data),

  // Xóa đơn đặt hàng
  delete: (id) => api.delete(`/purchase-orders/${id}`)
}; 