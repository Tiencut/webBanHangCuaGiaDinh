import api from './api';

const customerService = {
  getCustomers: (params) => {
    return api.get('/customers', { params });
  },
  getCustomerById: (id) => {
    return api.get(`/customers/${id}`);
  },
  createCustomer: (customerData) => {
    return api.post('/customers', customerData);
  },
  updateCustomer: (id, customerData) => {
    return api.put(`/customers/${id}`, customerData);
  },
  deleteCustomer: (id) => {
    return api.delete(`/customers/${id}`);
  },
};

export default customerService;