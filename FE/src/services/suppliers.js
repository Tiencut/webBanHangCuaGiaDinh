import api from './api'

export const suppliersAPI = {
  getAll: (page = 0, size = 20, sort = 'name') =>
    api.get(`/suppliers?page=${page}&size=${size}&sort=${sort}`)
}
