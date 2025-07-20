import api from './config.js'

export const categoriesApi = {
  // Lấy tất cả danh mục
  getAllCategories() {
    return api.get('/categories')
  },

  // Lấy danh mục theo ID
  getCategoryById(id) {
    return api.get(`/categories/${id}`)
  },

  // Lấy danh mục theo mã
  getCategoryByCode(code) {
    return api.get(`/categories/code/${code}`)
  },

  // Tạo danh mục mới
  createCategory(categoryData) {
    return api.post('/categories', categoryData)
  },

  // Cập nhật danh mục
  updateCategory(id, categoryData) {
    return api.put(`/categories/${id}`, categoryData)
  },

  // Xóa danh mục
  deleteCategory(id) {
    return api.delete(`/categories/${id}`)
  },

  // Lấy danh mục gốc
  getRootCategories() {
    return api.get('/categories/root')
  },

  // Lấy danh mục con
  getChildCategories(parentId) {
    return api.get(`/categories/${parentId}/children`)
  },

  // Lấy danh mục cha
  getParentCategory(childId) {
    return api.get(`/categories/${childId}/parent`)
  },

  // Lấy danh mục theo cấp độ
  getCategoriesByLevel(level) {
    return api.get(`/categories/level/${level}`)
  },

  // Tìm kiếm danh mục
  searchCategories(keyword) {
    return api.get('/categories/search', {
      params: { keyword }
    })
  },

  // Tìm kiếm nâng cao danh mục
  advancedSearch(searchRequest) {
    return api.post('/categories/search/advanced', searchRequest)
  },

  // Lấy danh mục theo thứ tự sắp xếp
  getCategoriesBySortOrder() {
    return api.get('/categories/sorted')
  },

  // Cập nhật thứ tự sắp xếp
  updateCategorySortOrder(id, newSortOrder) {
    return api.put(`/categories/${id}/sort-order`, null, {
      params: { newSortOrder }
    })
  }
} 