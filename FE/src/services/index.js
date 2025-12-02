import api from './api'

// Products API Service
const productsAPI = {
  // Get all products (with retry on transient failures)
  getAll: async (attempts = 2) => {
    const attempt = async (n) => {
      try {
        return await api.get('/products')
      } catch (err) {
        if (n <= 1) throw err
        // simple backoff
        await new Promise(res => setTimeout(res, 1000))
        return attempt(n - 1)
      }
    }
    return attempt(attempts)
  },

  // Get product by ID
  getById: (id) => api.get(`/products/${id}`),

  // Get product by code
  getByCode: (code) => api.get(`/products/code/${code}`),

  // Get product by part number
  getByPartNumber: (partNumber) => api.get(`/products/part-number/${partNumber}`),

  // Get products by category
  getByCategory: (categoryId) => api.get(`/products/category/${categoryId}`),

  // Get products by brand
  getByBrand: (brand) => api.get(`/products/brand/${brand}`),

  // Get products by vehicle type
  getByVehicleType: (vehicleType) => api.get(`/products/vehicle/${vehicleType}`),

  // Get low stock products
  getLowStock: () => api.get('/products/low-stock'),

  // Get products by price range
  getByPriceRange: (minPrice, maxPrice) => api.get(`/products/price-range?minPrice=${minPrice}&maxPrice=${maxPrice}`),

  // Get combo products
  getComboProducts: () => api.get('/products/combo'),

  // Create product
  create: (product) => api.post('/products', product),

  // Update product
  update: (id, product) => api.put(`/products/${id}`, product),

  // Delete product
  delete: (id) => api.delete(`/products/${id}`),

  // Search products
  search: (searchTerm, page = 0, size = 10) => api.get(`/products/search?q=${searchTerm}&page=${page}&size=${size}`),

  // Get products with inventory info
  getAllWithInventory: () => api.get('/products/with-inventory'),

  // Get product inventory by product ID
  getInventory: (productId) => api.get(`/products/${productId}/inventory`),

  // Get product total stock
  getTotalStock: (productId) => api.get(`/products/${productId}/total-stock`)
}

// Product Bundle API Service
const productBundlesAPI = {
  // Get all bundles
  getAll: () => api.get('/product-bundles'),

  // Get bundle by ID
  getById: (id) => api.get(`/product-bundles/${id}`),

  // Get bundles by parent product
  getByParentProduct: (parentId) => api.get(`/product-bundles/parent/${parentId}`),

  // Get bundles by child product
  getByChildProduct: (childId) => api.get(`/product-bundles/child/${childId}`),

  // Get all combo products
  getComboProducts: () => api.get('/product-bundles/combo-products'),

  // Get substitute products for a child
  getSubstituteProducts: (childId) => api.get(`/product-bundles/substitutes/${childId}`),

  // Create bundle
  create: (bundle) => api.post('/product-bundles', bundle),

  // Update bundle
  update: (id, bundle) => api.put(`/product-bundles/${id}`, bundle),

  // Delete bundle
  delete: (id) => api.delete(`/product-bundles/${id}`),

  // Add substitute product to bundle
  addSubstituteProduct: (bundleId, substituteId) => api.post(`/product-bundles/${bundleId}/substitutes/${substituteId}`),

  // Remove substitute product from bundle
  removeSubstituteProduct: (bundleId, substituteId) => api.delete(`/product-bundles/${bundleId}/substitutes/${substituteId}`),

  // Calculate bundle total price
  calculateTotalPrice: (parentId) => api.get(`/product-bundles/${parentId}/total-price`),

  // Check if product is combo
  isComboProduct: (productId) => api.get(`/product-bundles/${productId}/is-combo`),

  // Create combo from products
  createComboFromProducts: (parentId, childIds, quantities, prices) => api.post('/product-bundles/create-combo', {
    parentProductId: parentId,
    childProductIds: childIds,
    quantities: quantities,
    prices: prices
  })
}

// Customers API Service
const customersAPI = {
  // Get all customers
  getAll: () => api.get('/customers'),

  // Get customer by ID
  getById: (id) => api.get(`/customers/${id}`),

  // Create customer
  create: (customer) => api.post('/customers', customer),

  // Update customer
  update: (id, customer) => api.put(`/customers/${id}`, customer),

  // Delete customer
  delete: (id) => api.delete(`/customers/${id}`),

  // Search customers
  search: (searchTerm) => api.get(`/customers/search?q=${searchTerm}`)
}

// Orders API Service
const ordersAPI = {
  // Get all orders
  getAll: () => api.get('/orders'),

  // Get order by ID
  getById: (id) => api.get(`/orders/${id}`),

  // Create order
  create: (order) => api.post('/orders', order),

  // Update order
  update: (id, order) => api.put(`/orders/${id}`, order),

  // Update order status
  updateStatus: (id, status) => api.put(`/orders/${id}/status`, { status }),

  // Delete order
  delete: (id) => api.delete(`/orders/${id}`),

  // Get recent orders
  getRecent: (limit = 10) => api.get(`/orders/recent?limit=${limit}`),

  // Get orders by customer
  getByCustomer: (customerId) => api.get(`/orders/customer/${customerId}`),

  // Get orders by status
  getByStatus: (status) => api.get(`/orders/status/${status}`)
}

// Vehicles API Service
const vehiclesAPI = {
  // Get all vehicles
  getAll: () => api.get('/vehicles'),

  // Get vehicle by ID
  getById: (id) => api.get(`/vehicles/${id}`),

  // Create vehicle
  create: (vehicle) => api.post('/vehicles', vehicle),

  // Update vehicle
  update: (id, vehicle) => api.put(`/vehicles/${id}`, vehicle),

  // Delete vehicle
  delete: (id) => api.delete(`/vehicles/${id}`),

  // Search vehicles
  search: (keyword) => api.get(`/vehicles/search?keyword=${keyword}`),

  // Get compatible products for vehicle
  getCompatibleProducts: (vehicleId) => api.get(`/vehicles/${vehicleId}/products`),

  // Get popular vehicles
  getPopular: () => api.get('/vehicles/popular'),

  // Suggest vehicles for product
  suggestForProduct: (productId) => api.get(`/vehicles/suggest-for-product/${productId}`),

  // Get vehicle suggestions
  getSuggestions: (product, vehicle) => api.get(`/vehicles/suggest?product=${product}&vehicle=${vehicle}`)
}

// Suppliers API Service
const suppliersAPI = {
  // Get all suppliers (with pagination)
  getAll: (page = 0, size = 10, sort = 'name') => api.get(`/suppliers?page=${page}&size=${size}&sort=${sort}`),

  // Get supplier by ID
  getById: (id) => api.get(`/suppliers/${id}`),

  // Get supplier by code
  getByCode: (code) => api.get(`/suppliers/code/${code}`),

  // Create supplier
  create: (supplier) => api.post('/suppliers', supplier),

  // Update supplier
  update: (id, supplier) => api.put(`/suppliers/${id}`, supplier),

  // Delete supplier
  delete: (id) => api.delete(`/suppliers/${id}`),

  // Search suppliers
  search: (name, code) => {
    const params = new URLSearchParams()
    if (name) params.append('name', name)
    if (code) params.append('code', code)
    return api.get(`/suppliers/search?${params.toString()}`)
  },

  // Get active suppliers
  getActive: () => api.get('/suppliers/active'),

  // Get supplier count
  getCount: () => api.get('/suppliers/count'),

  // Get top suppliers
  getTop: (limit = 10) => api.get(`/suppliers/top?limit=${limit}`)
}

// Categories API Service
const categoriesAPI = {
  // Get category tree
  getTree: () => api.get('/categories/tree'),

  // Get all categories (with retry)
  getAll: async (attempts = 2) => {
    const attempt = async (n) => {
      try {
        return await api.get('/categories')
      } catch (err) {
        if (n <= 1) throw err
        await new Promise(res => setTimeout(res, 1000))
        return attempt(n - 1)
      }
    }
    return attempt(attempts)
  },

  // Get category by ID
  getById: (id) => api.get(`/categories/${id}`),

  // Create category
  create: (category) => api.post('/categories', category),

  // Update category
  update: (id, category) => api.put(`/categories/${id}`, category),

  // Delete category
  delete: (id) => api.delete(`/categories/${id}`),

  // Move category
  move: (id, parentId) => api.put(`/categories/${id}/move`, { parentId }),

  // Get category breadcrumb
  getBreadcrumb: (id) => api.get(`/categories/${id}/breadcrumb`),

  // Get products in category
  getProducts: (id, includeChildren = true) => api.get(`/categories/${id}/products?include_children=${includeChildren}`)
}

// Training API Service
const trainingAPI = {
  // Get training content
  getContent: () => api.get('/training/content'),

  // Get training by category
  getByCategory: (category) => api.get(`/training/category/${category}`),

  // Search training content
  search: (searchTerm) => api.get(`/training/search?q=${searchTerm}`),

  // Mark content as helpful
  markHelpful: (contentId) => api.post(`/training/${contentId}/helpful`),

  // Mark content as understood
  markUnderstood: (contentId) => api.post(`/training/${contentId}/understood`),

  // Get learning progress
  getProgress: (userId) => api.get(`/training/progress/${userId}`),

  // Create training content
  create: (content) => api.post('/training', content),

  // Update training content
  update: (id, content) => api.put(`/training/${id}`, content)
}

// Authentication API Service
const authAPI = {
  // Login
  login: (credentials) => api.post('/auth/login', credentials),

  // Register
  register: (userData) => api.post('/auth/register', userData),

  // Refresh token
  refreshToken: () => api.post('/auth/refresh-token'),

  // Logout
  logout: () => api.post('/auth/logout'),

  // Get current user
  getCurrentUser: () => api.get('/auth/me'),

  // Change password
  changePassword: (passwords) => api.put('/auth/change-password', passwords)
}

// Import/Export API Service
const importAPI = {
  // Import Excel file
  importExcel: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/import/excel', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // Download template
  downloadTemplate: () => api.get('/import/template', {
    responseType: 'blob'
  }),

  // Get import history
  getHistory: () => api.get('/import/history'),

  // Export data
  exportData: (type) => api.get(`/export/${type}`, {
    responseType: 'blob'
  })
}

// Dashboard/Analytics API Service
const dashboardAPI = {
  // Get dashboard stats
  getStats: () => api.get('/dashboard/stats'),

  // Get recent activities
  getRecentActivities: () => api.get('/dashboard/recent-activities'),

  // Get sales analytics
  getSalesAnalytics: (period = '7days') => api.get(`/dashboard/sales?period=${period}`),

  // Get inventory alerts
  getInventoryAlerts: () => api.get('/dashboard/inventory-alerts'),

  // Get top products
  getTopProducts: (limit = 10) => api.get(`/dashboard/top-products?limit=${limit}`),

  // Get revenue chart data
  getRevenueChart: (period = '30days') => api.get(`/dashboard/revenue?period=${period}`)
}

// Inventory API Service
const inventoryAPI = {
  // Get all inventory records
  getAll: () => api.get('/inventory'),

  // Get inventory by product ID
  getByProductId: (productId) => api.get(`/inventory/product/${productId}`),

  // Get inventory by supplier ID
  getBySupplierId: (supplierId) => api.get(`/inventory/supplier/${supplierId}`),

  // Get total stock for a product
  getTotalStock: (productId) => api.get(`/inventory/product/${productId}/total-stock`),

  // Get available stock for a product
  getAvailableStock: (productId) => api.get(`/inventory/product/${productId}/available-stock`),

  // Get reserved stock for a product
  getReservedStock: (productId) => api.get(`/inventory/product/${productId}/reserved-stock`),

  // Get supplier count for a product
  getSupplierCount: (productId) => api.get(`/inventory/product/${productId}/supplier-count`),

  // Get inventory details for a product
  getProductDetails: (productId) => api.get(`/inventory/product/${productId}/details`),

  // Stock in (add inventory)
  stockIn: (inventoryData) => api.post('/inventory/stock-in', inventoryData),

  // Stock out (remove inventory)
  stockOut: (inventoryData) => api.post('/inventory/stock-out', inventoryData),

  // Reserve stock
  reserveStock: (inventoryData) => api.post('/inventory/reserve', inventoryData),

  // Update inventory record
  update: (id, inventoryData) => api.put(`/inventory/${id}`, inventoryData),

  // Delete inventory record
  delete: (id) => api.delete(`/inventory/${id}`),

  // Get low stock alerts
  getLowStockAlerts: () => api.get('/inventory/low-stock-alerts'),

  // Get inventory statistics
  getStatistics: () => api.get('/inventory/statistics')
}

// Export all services
export {
  productsAPI,
  productBundlesAPI,
  customersAPI,
  ordersAPI,
  vehiclesAPI,
  suppliersAPI,
  categoriesAPI,
  trainingAPI,
  authAPI,
  importAPI,
  dashboardAPI,
  inventoryAPI
}
