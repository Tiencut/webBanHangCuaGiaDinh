import api from './config.js';

export const categoriesApi = {
  // Lấy tất cả categories
  getAll: () => {
    return api.get('/categories');
  },

  // Lấy categories gốc (level 0)
  getRoot: () => {
    return api.get('/categories/root');
  },

  // Lấy chi tiết category theo ID
  getById: (id) => {
    return api.get(`/categories/${id}`);
  },

  // Lấy category con
  getChildren: (id) => {
    return api.get(`/categories/${id}/children`);
  },

  // Tạo category mới
  create: (categoryData) => {
    return api.post('/categories', categoryData);
  },

  // Cập nhật category
  update: (id, categoryData) => {
    return api.put(`/categories/${id}`, categoryData);
  },

  // Di chuyển category
  move: (id, newParentId, newSortOrder) => {
    return api.put(`/categories/${id}/move`, {
      newParentId,
      newSortOrder
    });
  },

  // Xóa category
  delete: (id) => {
    return api.delete(`/categories/${id}`);
  },

  // Tìm kiếm category theo tên
  search: (name) => {
    return api.get(`/categories/search?name=${encodeURIComponent(name)}`);
  },

  // Lấy breadcrumb
  getBreadcrumb: (id) => {
    return api.get(`/categories/${id}/breadcrumb`);
  }
}; 