import { defineStore } from 'pinia'
import { vehiclesAPI } from '../services'

export const useVehiclesStore = defineStore('vehicles', {
  state: () => ({
    vehicles: [],
    loading: false,
    error: null,
    filters: {
      brand: null,
      type: null,
      searchTerm: ''
    }
  }),

  getters: {
    filteredVehicles: (state) => {
      let filtered = state.vehicles

      // Apply search filter
      if (state.filters.searchTerm) {
        const searchTerm = state.filters.searchTerm.toLowerCase()
        filtered = filtered.filter(vehicle =>
          vehicle.name.toLowerCase().includes(searchTerm) ||
          vehicle.brand.toLowerCase().includes(searchTerm)
        )
      }

      // Apply brand filter
      if (state.filters.brand) {
        filtered = filtered.filter(vehicle => vehicle.brand === state.filters.brand)
      }

      // Apply type filter
      if (state.filters.type) {
        filtered = filtered.filter(vehicle => vehicle.type === state.filters.type)
      }

      return filtered
    },

    brands: (state) => {
      const brandCounts = state.vehicles.reduce((acc, vehicle) => {
        acc[vehicle.brand] = (acc[vehicle.brand] || 0) + 1
        return acc
      }, {})
      
      return Object.entries(brandCounts).map(([name, count]) => ({ name, count }))
    },

    popularBrands: (state) => {
      return state.vehicles
        .reduce((acc, vehicle) => {
          acc[vehicle.brand] = (acc[vehicle.brand] || 0) + 1
          return acc
        }, {})
    }
  },

  actions: {
    async fetchVehicles() {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.getAll()
        this.vehicles = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching vehicles:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchVehicleById(id) {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.getById(id)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching vehicle:', error)
        return null
      } finally {
        this.loading = false
      }
    },

    async createVehicle(vehicle) {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.create(vehicle)
        this.vehicles.push(response.data)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error creating vehicle:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async updateVehicle(id, vehicle) {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.update(id, vehicle)
        const index = this.vehicles.findIndex(v => v.id === id)
        if (index !== -1) {
          this.vehicles[index] = response.data
        }
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error updating vehicle:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async deleteVehicle(id) {
      this.loading = true
      this.error = null
      
      try {
        await vehiclesAPI.delete(id)
        this.vehicles = this.vehicles.filter(v => v.id !== id)
      } catch (error) {
        this.error = error.message
        console.error('Error deleting vehicle:', error)
        throw error
      } finally {
        this.loading = false
      }
    },

    async searchVehicles(keyword) {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.search(keyword)
        this.vehicles = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error searching vehicles:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchCompatibleProducts(vehicleId) {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.getCompatibleProducts(vehicleId)
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching compatible products:', error)
        return []
      } finally {
        this.loading = false
      }
    },

    async fetchPopularVehicles() {
      this.loading = true
      this.error = null
      
      try {
        const response = await vehiclesAPI.getPopular()
        return response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching popular vehicles:', error)
        return []
      } finally {
        this.loading = false
      }
    },

    // Filter actions
    setSearchTerm(searchTerm) {
      this.filters.searchTerm = searchTerm
    },

    setBrand(brand) {
      this.filters.brand = brand
    },

    setType(type) {
      this.filters.type = type
    },

    clearFilters() {
      this.filters = {
        brand: null,
        type: null,
        searchTerm: ''
      }
    }
  }
})
