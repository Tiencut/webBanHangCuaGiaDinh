import { defineStore } from 'pinia'
import { authAPI } from '../services'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        token: localStorage.getItem('auth_token'),
        loading: false,
        error: null
    }),

    getters: {
        isAuthenticated: (state) => !!state.token,
        userRole: (state) => state.user ? .role,
        userName: (state) => state.user ? .name || state.user ? .username,
        isAdmin: (state) => state.user ? .role === 'ADMIN',
        isManager: (state) => state.user ? .role === 'MANAGER',
        isStaff: (state) => state.user ? .role === 'STAFF'
    },

    actions: {
        async login(credentials) {
            this.loading = true
            this.error = null

            try {
                const response = await authAPI.login(credentials)
                const { token, refreshToken, user } = response.data

                this.token = token
                this.user = user
                localStorage.setItem('auth_token', token)
                if (refreshToken) localStorage.setItem('refresh_token', refreshToken)

                return { success: true, user }
            } catch (error) {
                this.error = error.response ? .data ? .message || error.message
                console.error('Login error:', error)
                return { success: false, error: this.error }
            } finally {
                this.loading = false
            }
        },

        async register(userData) {
            this.loading = true
            this.error = null

            try {
                const response = await authAPI.register(userData)
                return { success: true, data: response.data }
            } catch (error) {
                this.error = error.response ? .data ? .message || error.message
                console.error('Registration error:', error)
                return { success: false, error: this.error }
            } finally {
                this.loading = false
            }
        },

        async logout() {
            this.loading = true

            try {
                const refresh = localStorage.getItem('refresh_token')
                await authAPI.logout(refresh)
            } catch (error) {
                console.error('Logout error:', error)
            } finally {
                this.token = null
                this.user = null
                localStorage.removeItem('auth_token')
                localStorage.removeItem('refresh_token')
                this.loading = false
            }
        },

        async getCurrentUser() {
            if (!this.token) return null

            this.loading = true
            this.error = null

            try {
                const response = await authAPI.getCurrentUser()
                this.user = response.data
                return response.data
            } catch (error) {
                this.error = error.message
                console.error('Error getting current user:', error)
                    // If token is invalid, logout
                if (error.response ? .status === 401) {
                    this.logout()
                }
                return null
            } finally {
                this.loading = false
            }
        },

        async refreshToken() {
            const refresh = localStorage.getItem('refresh_token')
            if (!refresh) return false

            try {
                const response = await authAPI.refreshToken(refresh)
                const { token, refreshToken } = response.data
                this.token = token
                if (refreshToken) localStorage.setItem('refresh_token', refreshToken)
                localStorage.setItem('auth_token', token)
                return true
            } catch (error) {
                console.error('Token refresh error:', error)
                this.logout()
                return false
            }
        },

        async changePassword(passwords) {
            this.loading = true
            this.error = null

            try {
                await authAPI.changePassword(passwords)
                return { success: true }
            } catch (error) {
                this.error = error.response ? .data ? .message || error.message
                console.error('Change password error:', error)
                return { success: false, error: this.error }
            } finally {
                this.loading = false
            }
        },

        // Initialize auth state from localStorage
        initAuth() {
            const token = localStorage.getItem('auth_token')
            if (token) {
                this.token = token
                this.getCurrentUser()
            }
        },

        // Check if user has permission
        hasPermission(permission) {
            if (!this.user) return false

            const rolePermissions = {
                ADMIN: ['*'], // Admin has all permissions
                MANAGER: [
                    'products.read', 'products.create', 'products.update', 'products.delete',
                    'customers.read', 'customers.create', 'customers.update', 'customers.delete',
                    'orders.read', 'orders.create', 'orders.update', 'orders.delete',
                    'vehicles.read', 'vehicles.create', 'vehicles.update', 'vehicles.delete',
                    'suppliers.read', 'suppliers.create', 'suppliers.update', 'suppliers.delete',
                    'import.read', 'import.create',
                    'dashboard.read'
                ],
                STAFF: [
                    'products.read',
                    'customers.read', 'customers.create', 'customers.update',
                    'orders.read', 'orders.create', 'orders.update',
                    'vehicles.read',
                    'suppliers.read',
                    'dashboard.read'
                ]
            }

            const userPermissions = rolePermissions[this.user.role] || []
            return userPermissions.includes('*') || userPermissions.includes(permission)
        }
    }
})