import api from './config.js';

export const productsAPI = {
  // Lấy danh sách sản phẩm với phân trang
  getProducts: (page = 0, size = 10, search = '', categoryId = null, supplierId = null) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    if (search) params.append('search', search);
    if (categoryId) params.append('categoryId', categoryId);
    if (supplierId) params.append('supplierId', supplierId);
    
    return api.get(`/products?${params.toString()}`);
  },

  // Lấy chi tiết sản phẩm theo ID
  getProductById: (id) => {
    return api.get(`/products/${id}`);
  },

  // Tạo sản phẩm mới
  createProduct: (productData) => {
    return api.post('/products', productData);
  },

  // Cập nhật sản phẩm
  updateProduct: (id, productData) => {
    return api.put(`/products/${id}`, productData);
  },

  // Xóa sản phẩm
  deleteProduct: (id) => {
    return api.delete(`/products/${id}`);
  },

  // Lấy danh sách categories
  getCategories: () => {
    return api.get('/categories');
  },

  // Lấy danh sách suppliers
  getSuppliers: () => {
    return api.get('/suppliers');
  }
}; 