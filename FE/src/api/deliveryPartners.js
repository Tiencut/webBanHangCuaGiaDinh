import api from './config.js';

export const deliveryPartnersApi = {
  // Lấy danh sách đối tác giao hàng
  getAll: (page = 0, size = 10, search = '', type = null, status = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (type) params.append('type', type);
    if (status) params.append('status', status);
    return api.get(`/delivery-partners?${params.toString()}`);
  },

  // Lấy chi tiết đối tác giao hàng
  getById: (id) => api.get(`/delivery-partners/${id}`),

  // Tạo mới đối tác giao hàng
  create: (data) => api.post('/delivery-partners', data),

  // Cập nhật đối tác giao hàng
  update: (id, data) => api.put(`/delivery-partners/${id}`, data),

  // Xóa đối tác giao hàng
  delete: (id) => api.delete(`/delivery-partners/${id}`)
}; 