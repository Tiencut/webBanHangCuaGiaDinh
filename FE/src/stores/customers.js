import { defineStore } from 'pinia'
import { customersAPI } from '../services'

export const useCustomersStore = defineStore('customers', {
  state: () => ({
    customers: [],
    loading: false,
    error: null,
    filters: {
      type: null,
      status: null,
      searchTerm: ''
    }
  }),

  getters: {
    filteredCustomers: (state) => {
      let filtered = state.customers

      // Apply search filter
      if (state.filters.searchTerm) {
        const searchTerm = state.filters.searchTerm.toLowerCase()
        filtered = filtered.filter(customer =>
          customer.name.toLowerCase().includes(searchTerm) ||
          customer.email?.toLowerCase().includes(searchTerm) ||
          customer.phone?.includes(searchTerm)
        )
      }

      // Apply type filter
      if (state.filters.type) {
        filtered = filtered.filter(customer => customer.type === state.filters.type)
      }

      // Apply status filter
      if (state.filters.status) {
        filtered = filtered.filter(customer => customer.status === state.filters.status)
      }

      return filtered
    },

    activeCustomers: (state) => {
      return state.customers.filter(customer => customer.status === 'ACTIVE')
    },

    vipCustomers: (state) => {
      return state.customers.filter(customer => customer.type === 'VIP')
    }
  },

  actions: {
    async fetchCustomers() {
      this.loading = true
      this.error = null
      
      try {
        const response = await customersAPI.getAll()
        this.customers = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching customers:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchCustomerById(id) {
      this.loading = true
      this.error = null
      
      try {
        const response = await customersAPI.getById(id)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching customer:', error)
        return null
      } finally {
        this.loading = false
      }
    },

    async createCustomer(customer) {
      this.loading = true
      this.error = null
      
      try {
        const response = await customersAPI.create(customer)
        this.customers.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error creating customer:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateCustomer(id, customer) {
      this.loading = true
      this.error = null
      
      try {
        const response = await customersAPI.update(id, customer)
        const index = this.customers.findIndex(c => c.id === id)
        if (index !== -1) {
          this.customers[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error updating customer:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteCustomer(id) {
      this.loading = true
      this.error = null
      
      try {
        await customersAPI.delete(id)
        this.customers = this.customers.filter(c => c.id !== id)
      } catch (error) {
        this.error = error.message
        console.error('Error deleting customer:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async searchCustomers(searchTerm) {
      this.loading = true
      this.error = null
      
      try {
        const response = await customersAPI.search(searchTerm)
        this.customers = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error searching customers:', error)
      } finally {
        this.loading = false
      }
    },

    // Filter actions
    setSearchTerm(searchTerm) {
      this.filters.searchTerm = searchTerm
    },

    setType(type) {
      this.filters.type = type
    },

    setStatus(status) {
      this.filters.status = status
    },

    clearFilters() {
      this.filters = {
        type: null,
        status: null,
        searchTerm: ''
      }
    }
  }
})
