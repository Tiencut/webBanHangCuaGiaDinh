import api from './config.js';

export const vehiclesApi = {
  // Lấy tất cả vehicle models
  getAll: (page = 0, size = 10, search = '', brand = null, year = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (brand) params.append('brand', brand);
    if (year) params.append('year', year);
    
    return api.get(`/vehicles?${params.toString()}`);
  },

  // Lấy chi tiết vehicle model theo ID
  getById: (id) => {
    return api.get(`/vehicles/${id}`);
  },

  // Tạo vehicle model mới
  create: (vehicleData) => {
    return api.post('/vehicles', vehicleData);
  },

  // Cập nhật vehicle model
  update: (id, vehicleData) => {
    return api.put(`/vehicles/${id}`, vehicleData);
  },

  // Xóa vehicle model
  delete: (id) => {
    return api.delete(`/vehicles/${id}`);
  },

  // Gợi ý sản phẩm theo xe và từ khóa
  suggest: (vehicleId, keyword) => {
    return api.get(`/vehicles/suggest?vehicleId=${vehicleId}&keyword=${encodeURIComponent(keyword)}`);
  },

  // Tìm kiếm vehicle models thông minh
  search: (query) => {
    return api.get(`/vehicles/search?q=${encodeURIComponent(query)}`);
  },

  // Lấy sản phẩm tương thích với xe
  getCompatibleProducts: (id) => {
    return api.get(`/vehicles/${id}/products`);
  },

  // Lấy xe theo thương hiệu
  getByBrand: (brand) => {
    return api.get(`/vehicles/brand/${brand}`);
  },

  // Lấy xe theo năm sản xuất
  getByYear: (year) => {
    return api.get(`/vehicles/year/${year}`);
  },

  // Lấy xe theo động cơ
  getByEngine: (engine) => {
    return api.get(`/vehicles/engine/${engine}`);
  },

  // Lấy xe phổ biến
  getPopular: () => {
    return api.get('/vehicles/popular');
  },

  // Gợi ý xe cho sản phẩm
  suggestForProduct: (productId) => {
    return api.get(`/vehicles/suggest-for-product/${productId}`);
  }
}; 