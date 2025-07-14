import { defineStore } from 'pinia'
import { dashboardAPI } from '../services'

export const useDashboardStore = defineStore('dashboard', {
  state: () => ({
    stats: {
      totalProducts: 0,
      totalCustomers: 0,
      totalOrders: 0,
      totalRevenue: 0,
      lowStockCount: 0,
      pendingOrdersCount: 0
    },
    recentOrders: [],
    recentActivities: [],
    salesChart: {
      labels: [],
      data: []
    },
    revenueChart: {
      labels: [],
      data: []
    },
    topProducts: [],
    inventoryAlerts: [],
    loading: false,
    error: null
  }),

  getters: {
    formattedStats: (state) => ({
      totalProducts: state.stats.totalProducts.toLocaleString(),
      totalCustomers: state.stats.totalCustomers.toLocaleString(),
      totalOrders: state.stats.totalOrders.toLocaleString(),
      totalRevenue: new Intl.NumberFormat('vi-VN', { 
        style: 'currency', 
        currency: 'VND' 
      }).format(state.stats.totalRevenue),
      lowStockCount: state.stats.lowStockCount.toLocaleString(),
      pendingOrdersCount: state.stats.pendingOrdersCount.toLocaleString()
    }),

    criticalAlerts: (state) => {
      return state.inventoryAlerts.filter(alert => alert.severity === 'CRITICAL')
    },

    warningAlerts: (state) => {
      return state.inventoryAlerts.filter(alert => alert.severity === 'WARNING')
    }
  },

  actions: {
    async fetchDashboardStats() {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getStats()
        this.stats = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching dashboard stats:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchRecentActivities() {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getRecentActivities()
        this.recentActivities = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching recent activities:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchSalesAnalytics(period = '7days') {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getSalesAnalytics(period)
        this.salesChart = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching sales analytics:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchInventoryAlerts() {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getInventoryAlerts()
        this.inventoryAlerts = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching inventory alerts:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchTopProducts(limit = 10) {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getTopProducts(limit)
        this.topProducts = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching top products:', error)
      } finally {
        this.loading = false
      }
    },

    async fetchRevenueChart(period = '30days') {
      this.loading = true
      this.error = null
      
      try {
        const response = await dashboardAPI.getRevenueChart(period)
        this.revenueChart = response.data
      } catch (error) {
        this.error = error.message
        console.error('Error fetching revenue chart:', error)
      } finally {
        this.loading = false
      }
    },

    async refreshDashboard() {
      await Promise.all([
        this.fetchDashboardStats(),
        this.fetchRecentActivities(),
        this.fetchSalesAnalytics(),
        this.fetchInventoryAlerts(),
        this.fetchTopProducts(),
        this.fetchRevenueChart()
      ])
    }
  }
})
