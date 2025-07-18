import api from './config.js';

export const suppliersApi = {
  // Lấy danh sách nhà cung cấp với phân trang
  getAll: (page = 0, size = 10, search = '', brand = null, status = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (brand) params.append('brand', brand);
    if (status) params.append('status', status);
    
    return api.get(`/suppliers?${params.toString()}`);
  },

  // Lấy chi tiết nhà cung cấp theo ID
  getSupplierById: (id) => {
    return api.get(`/suppliers/${id}`);
  },

  // Tạo nhà cung cấp mới
  create: (supplierData) => {
    return api.post('/suppliers', supplierData);
  },

  // Cập nhật nhà cung cấp
  update: (id, supplierData) => {
    return api.put(`/suppliers/${id}`, supplierData);
  },

  // Xóa nhà cung cấp
  delete: (id) => {
    return api.delete(`/suppliers/${id}`);
  },

  // Cập nhật rating nhà cung cấp
  updateRating: (id, rating) => {
    return api.put(`/suppliers/${id}/rating`, { rating });
  },

  // Chuyển đổi trạng thái nhà cung cấp
  toggleStatus: (id) => {
    return api.put(`/suppliers/${id}/toggle-status`);
  },

  // Blacklist nhà cung cấp
  blacklist: (id) => {
    return api.put(`/suppliers/${id}/blacklist`);
  },

  // Lấy nhà cung cấp theo brand
  getByBrand: (brand) => {
    return api.get(`/suppliers/vehicle-brand/${brand}`);
  },

  // Lấy nhà cung cấp active
  getActive: () => {
    return api.get('/suppliers/active');
  },

  // Lấy top nhà cung cấp
  getTop: () => {
    return api.get('/suppliers/top');
  }
}; 