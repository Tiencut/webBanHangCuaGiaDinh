import api from './config.js';

export const importApi = {
  // Upload file import
  upload: (formData) => api.post('/import/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  }),

  // Lấy danh sách file đã import
  getAll: (page = 0, size = 20) => {
    const params = new URLSearchParams();
    params.append('page', page);
    params.append('size', size);
    return api.get(`/import/files?${params.toString()}`);
  },

  // Lấy chi tiết file import
  getById: (id) => api.get(`/import/files/${id}`)
}; 