import api from './config'

export const inventoryApi = {
  // Lấy danh sách tồn kho
  getAll: (page = 0, size = 10, search = '', categoryId = null, supplierId = null) => {
    return api.get('/inventory', {
      params: { page, size, search, categoryId, supplierId }
    })
  },

  // Lấy chi tiết tồn kho theo sản phẩm
  getProductDetails: (productId) => {
    return api.get(`/inventory/products/${productId}`)
  },

  // Lấy tồn kho theo nhà cung cấp
  getSupplierInventory: (supplierId) => {
    return api.get(`/inventory/suppliers/${supplierId}`)
  },

  // Cập nhật tồn kho
  updateStock: (productId, supplierId, quantity) => {
    return api.put(`/inventory/products/${productId}/suppliers/${supplierId}`, {
      quantity
    })
  },

  // Tạo phiếu kiểm kho
  createCheck: (checkData) => {
    return api.post('/inventory/checks', checkData)
  },

  // Lấy danh sách phiếu kiểm kho
  getChecks: (page = 0, size = 10, status = null) => {
    return api.get('/inventory/checks', {
      params: { page, size, status }
    })
  },

  // Lấy chi tiết phiếu kiểm kho
  getCheckById: (checkId) => {
    return api.get(`/inventory/checks/${checkId}`)
  },

  // Cập nhật phiếu kiểm kho
  updateCheck: (checkId, checkData) => {
    return api.put(`/inventory/checks/${checkId}`, checkData)
  },

  // Xóa phiếu kiểm kho
  deleteCheck: (checkId) => {
    return api.delete(`/inventory/checks/${checkId}`)
  },

  // Lấy thống kê tồn kho
  getStats: () => {
    return api.get('/inventory/stats')
  },

  // Lấy sản phẩm sắp hết hàng
  getLowStock: (threshold = 10) => {
    return api.get('/inventory/low-stock', {
      params: { threshold }
    })
  },

  // Lấy sản phẩm hết hàng
  getOutOfStock: () => {
    return api.get('/inventory/out-of-stock')
  },

  // Export báo cáo tồn kho
  exportReport: (format = 'excel') => {
    return api.get('/inventory/export', {
      params: { format },
      responseType: 'blob'
    })
  }
} 