import api from './config.js'

export const notesApi = {
  list: (userId, { page = 0, size = 20, keyword = '', completed = null } = {}) => {
    const params = new URLSearchParams()
    params.append('userId', userId)
    params.append('page', page)
    params.append('size', size)
    if (keyword) params.append('keyword', keyword)
    if (completed !== null && completed !== undefined) params.append('completed', completed)
    return api.get(`/notes?${params.toString()}`)
  },

  get: (id) => api.get(`/notes/${id}`),

  create: (note) => api.post('/notes', note),

  update: (id, note) => api.put(`/notes/${id}`, note),

  delete: (id) => api.delete(`/notes/${id}`)
}

