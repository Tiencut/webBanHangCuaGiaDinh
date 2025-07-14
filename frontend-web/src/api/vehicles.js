import api from './config.js';

export const vehiclesApi = {
  // Lấy danh sách mẫu xe với phân trang
  getAll: (page = 0, size = 10, search = '', brand = null, type = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (brand) params.append('brand', brand);
    if (type) params.append('type', type);
    
    return api.get(`/vehicles?${params.toString()}`);
  },

  // Lấy chi tiết mẫu xe theo ID
  getVehicleById: (id) => {
    return api.get(`/vehicles/${id}`);
  },

  // Tạo mẫu xe mới
  create: (vehicleData) => {
    return api.post('/vehicles', vehicleData);
  },

  // Cập nhật mẫu xe
  update: (id, vehicleData) => {
    return api.put(`/vehicles/${id}`, vehicleData);
  },

  // Xóa mẫu xe
  delete: (id) => {
    return api.delete(`/vehicles/${id}`);
  },

  // Lấy danh sách phụ tùng tương thích với mẫu xe
  getCompatibleParts: (vehicleId) => {
    return api.get(`/vehicles/${vehicleId}/compatible-parts`);
  },

  // Lấy danh sách brands
  getBrands: () => {
    return api.get('/vehicles/brands');
  }
}; 