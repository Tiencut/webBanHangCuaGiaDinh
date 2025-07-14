import { defineStore } from 'pinia'
import { ordersAPI } from '../services'

export const useOrdersStore = defineStore('orders', {
  state: () => ({
    orders: [],
    loading: false,
    error: null,
    filters: {
      status: null,
      customerId: null,
      searchTerm: '',
      dateRange: { start: null, end: null }
    }
  }),

  getters: {
    filteredOrders: (state) => {
      let filtered = state.orders

      // Apply search filter
      if (state.filters.searchTerm) {
        const searchTerm = state.filters.searchTerm.toLowerCase()
        filtered = filtered.filter(order =>
          order.orderNumber.toLowerCase().includes(searchTerm) ||
          order.customerName?.toLowerCase().includes(searchTerm)
        )
      }

      // Apply status filter
      if (state.filters.status) {
        filtered = filtered.filter(order => order.status === state.filters.status)
      }

      // Apply customer filter
      if (state.filters.customerId) {
        filtered = filtered.filter(order => order.customerId === state.filters.customerId)
      }

      // Apply date range filter
      if (state.filters.dateRange.start) {
        filtered = filtered.filter(order => new Date(order.createdAt) >= new Date(state.filters.dateRange.start))
      }
      if (state.filters.dateRange.end) {
        filtered = filtered.filter(order => new Date(order.createdAt) <= new Date(state.filters.dateRange.end))
      }

      return filtered
    },

    pendingOrders: (state) => {
      return state.orders.filter(order => order.status === 'PENDING')
    },

    completedOrders: (state) => {
      return state.orders.filter(order => order.status === 'COMPLETED')
    },

    totalRevenue: (state) => {
      return state.orders
        .filter(order => order.status === 'COMPLETED')
        .reduce((sum, order) => sum + order.totalAmount, 0)
    }
  },

  actions: {
    async fetchOrders() {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.getAll()
        this.orders = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching orders:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchOrderById(id) {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.getById(id)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching order:', error)
        return null
      } finally {
        this.loading = false
      }
    },

    async createOrder(order) {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.create(order)
        this.orders.unshift(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error creating order:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateOrder(id, order) {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.update(id, order)
        const index = this.orders.findIndex(o => o.id === id)
        if (index !== -1) {
          this.orders[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error updating order:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateOrderStatus(id, status) {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.updateStatus(id, status)
        const index = this.orders.findIndex(o => o.id === id)
        if (index !== -1) {
          this.orders[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error updating order status:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteOrder(id) {
      this.loading = true
      this.error = null
      
      try {
        await ordersAPI.delete(id)
        this.orders = this.orders.filter(o => o.id !== id)
      } catch (error) {
        this.error = error.message
        console.error('Error deleting order:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async fetchRecentOrders(limit = 10) {
      this.loading = true
      this.error = null
      
      try {
        const response = await ordersAPI.getRecent(limit)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching recent orders:', error)
        return []
      } finally {
        this.loading = false
      }
    },

    // Filter actions
    setSearchTerm(searchTerm) {
      this.filters.searchTerm = searchTerm
    },

    setStatus(status) {
      this.filters.status = status
    },

    setCustomerId(customerId) {
      this.filters.customerId = customerId
    },

    setDateRange(start, end) {
      this.filters.dateRange = { start, end }
    },

    clearFilters() {
      this.filters = {
        status: null,
        customerId: null,
        searchTerm: '',
        dateRange: { start: null, end: null }
      }
    }
  }
})
