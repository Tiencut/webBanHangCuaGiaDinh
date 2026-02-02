import axios from 'axios';

// Tạo instance axios với cấu hình cơ bản
// Use Vite env variable if provided, otherwise use relative path so dev-server proxy works
const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Attach Authorization header when token exists
api.interceptors.request.use((config) => {
    try {
        const token = localStorage.getItem('auth_token');
        if (token) {
            config.headers = config.headers || {};
            config.headers['Authorization'] = `Bearer ${token}`;
        }
    } catch (err) {
        // ignore
    }
    return config;
});

// Interceptor để xử lý response
api.interceptors.response.use(
    (response) => response,
    (error) => {
        // Improve error message for network errors (backend not running / connection refused)
        if (error.code === 'ERR_NETWORK' || !error.response) {
            console.error('API Network Error: backend may be unavailable:', error.message);
        } else {
            console.error('API Error:', error);
        }
        return Promise.reject(error);
    }
);

// Request interceptor: attach Authorization header if token present
api.interceptors.request.use(
    (config) => {
        try {
            const token = localStorage.getItem('auth_token')
            if (token) {
                config.headers = config.headers || {}
                config.headers['Authorization'] = `Bearer ${token}`
            }
        } catch (e) {
            // ignore
        }
        return config
    },
    (error) => Promise.reject(error)
)

export default api;