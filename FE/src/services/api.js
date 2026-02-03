import axios from 'axios'

// Base API configuration
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

// Create axios instance with base configuration
// Increase timeout to 30s to avoid transient timeouts when backend is slow
const api = axios.create({
    baseURL: API_BASE_URL,
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// Request interceptor for adding auth token
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('auth_token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// Response interceptor for handling common errors
api.interceptors.response.use(
    (response) => response,
    async(error) => {
        const originalRequest = error.config
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true
            const refreshToken = localStorage.getItem('refresh_token')
            if (refreshToken) {
                try {
                    const resp = await axios.post(`${API_BASE_URL}/auth/refresh-token`, null, { params: { refreshToken } })
                    const newToken = resp.data?.token
                    const newRefresh = resp.data?.refreshToken
                    if (newToken) {
                        localStorage.setItem('auth_token', newToken)
                        if (newRefresh) localStorage.setItem('refresh_token', newRefresh)
                        originalRequest.headers['Authorization'] = `Bearer ${newToken}`
                        return api(originalRequest)
                    }
                } catch (refreshErr) {
                    console.error('Refresh failed', refreshErr)
                }
            }
            // fallback: clear auth and redirect
            localStorage.removeItem('auth_token')
            localStorage.removeItem('refresh_token')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default api