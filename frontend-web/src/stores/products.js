import { defineStore } from 'pinia'
import { productsAPI } from '../services'

export const useProductsStore = defineStore('products', {
  state: () => ({
    products: [],
    loading: false,
    error: null,
    filters: {
      category: null,
      brand: null,
      vehicleType: null,
      searchTerm: '',
      priceRange: { min: null, max: null }
    },
    pagination: {
      page: 0,
      size: 10,
      total: 0
    }
  }),

  getters: {
    filteredProducts: (state) => {
      let filtered = state.products

      // Apply search filter
      if (state.filters.searchTerm) {
        const searchTerm = state.filters.searchTerm.toLowerCase()
        filtered = filtered.filter(product =>
          product.name.toLowerCase().includes(searchTerm) ||
          product.code.toLowerCase().includes(searchTerm) ||
          product.partNumber?.toLowerCase().includes(searchTerm)
        )
      }

      // Apply category filter
      if (state.filters.category) {
        filtered = filtered.filter(product => product.categoryId === state.filters.category)
      }

      // Apply brand filter
      if (state.filters.brand) {
        filtered = filtered.filter(product => product.brand === state.filters.brand)
      }

      // Apply vehicle type filter
      if (state.filters.vehicleType) {
        filtered = filtered.filter(product => product.vehicleType === state.filters.vehicleType)
      }

      // Apply price range filter
      if (state.filters.priceRange.min !== null) {
        filtered = filtered.filter(product => product.salePrice >= state.filters.priceRange.min)
      }
      if (state.filters.priceRange.max !== null) {
        filtered = filtered.filter(product => product.salePrice <= state.filters.priceRange.max)
      }

      return filtered
    },

    lowStockProducts: (state) => {
      return state.products.filter(product => product.quantity <= product.minQuantity)
    },

    outOfStockProducts: (state) => {
      return state.products.filter(product => product.quantity === 0)
    }
  },

  actions: {
    async fetchProducts() {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.getAll()
        this.products = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching products:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchProductById(id) {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.getById(id)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching product:', error)
        return null
      } finally {
        this.loading = false
      }
    },

    async createProduct(product) {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.create(product)
        this.products.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error creating product:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateProduct(id, product) {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.update(id, product)
        const index = this.products.findIndex(p => p.id === id)
        if (index !== -1) {
          this.products[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error updating product:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteProduct(id) {
      this.loading = true
      this.error = null
      
      try {
        await productsAPI.delete(id)
        this.products = this.products.filter(p => p.id !== id)
      } catch (error) {
        this.error = error.message
        console.error('Error deleting product:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async searchProducts(searchTerm) {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.search(searchTerm)
        this.products = response.data.content || response.data
        this.pagination.total = response.data.totalElements || response.data.length
      } catch (error) {
        this.error = error.message
        console.error('Error searching products:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchLowStockProducts() {
      this.loading = true
      this.error = null
      
      try {
        const response = await productsAPI.getLowStock()
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching low stock products:', error)
        return []
      } finally {
        this.loading = false
      }
    },

    // Filter actions
    setSearchTerm(searchTerm) {
      this.filters.searchTerm = searchTerm
    },

    setCategory(category) {
      this.filters.category = category
    },

    setBrand(brand) {
      this.filters.brand = brand
    },

    setVehicleType(vehicleType) {
      this.filters.vehicleType = vehicleType
    },

    setPriceRange(min, max) {
      this.filters.priceRange = { min, max }
    },

    clearFilters() {
      this.filters = {
        category: null,
        brand: null,
        vehicleType: null,
        searchTerm: '',
        priceRange: { min: null, max: null }
      }
    }
  }
})
