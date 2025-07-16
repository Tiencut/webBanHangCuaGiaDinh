<template>
  <div class="suppliers-page">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Nhà cung cấp</h1>
      <p class="text-gray-600">Quản lý thông tin nhà cung cấp và đối tác</p>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Tổng nhà cung cấp</p>
            <p class="text-2xl font-bold text-gray-900">{{ totalSuppliers }}</p>
          </div>
          <div class="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Đang hợp tác</p>
            <p class="text-2xl font-bold text-green-600">{{ activeSuppliers }}</p>
          </div>
          <div class="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Tổng giá trị</p>
            <p class="text-2xl font-bold text-purple-600">₫{{ formatCurrency(totalValue) }}</p>
          </div>
          <div class="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm font-medium text-gray-600">Đơn nhập tháng</p>
            <p class="text-2xl font-bold text-orange-600">{{ monthlyOrders }}</p>
          </div>
          <div class="h-12 w-12 bg-orange-100 rounded-full flex items-center justify-center">
            <svg class="h-6 w-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Suppliers Management -->
    <div class="card">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-semibold text-gray-900">Danh sách nhà cung cấp</h2>
        <div class="flex items-center space-x-3">
          <div class="flex items-center space-x-2">
            <input v-model="searchTerm" type="text" placeholder="Tìm nhà cung cấp..." 
                   class="form-input w-64">
            <select v-model="statusFilter" class="form-input w-32">
              <option value="">Tất cả</option>
              <option value="active">Hoạt động</option>
              <option value="inactive">Tạm dừng</option>
            </select>
          </div>
          <button @click="showAddSupplierModal = true" class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Thêm nhà cung cấp
          </button>
        </div>
      </div>

      <!-- Suppliers Table -->
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th>STT</th>
              <th>Tên nhà cung cấp</th>
              <th>Mã</th>
              <th>Địa chỉ</th>
              <th>Số điện thoại</th>
              <th>Email</th>
              <th>Người liên hệ</th>
              <th>Mã số thuế</th>
              <th>Số tài khoản</th>
              <th>Tên ngân hàng</th>
              <th>Điều khoản thanh toán</th>
              <th>Hạn mức tín dụng</th>
              <th>Thời gian giao hàng</th>
              <th>Thương hiệu xe</th>
              <th>Đánh giá</th>
              <th>Ghi chú</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(supplier, idx) in filteredSuppliers" :key="supplier.id">
              <td>{{ idx + 1 }}</td>
              <td>{{ supplier.name }}</td>
              <td>{{ supplier.code }}</td>
              <td>{{ supplier.address }}</td>
              <td>{{ supplier.phone }}</td>
              <td>{{ supplier.email }}</td>
              <td>{{ supplier.contactPerson }}</td>
              <td>{{ supplier.taxCode }}</td>
              <td>{{ supplier.bankAccount }}</td>
              <td>{{ supplier.bankName }}</td>
              <td>{{ supplier.paymentTerms }}</td>
              <td>{{ supplier.creditLimit }}</td>
              <td>{{ supplier.deliveryTimeDays }}</td>
              <td>{{ supplier.vehicleBrands }}</td>
              <td>{{ supplier.rating }}</td>
              <td>{{ supplier.notes }}</td>
              <td>
                <span :class="supplier.status === 'ACTIVE' ? 'bg-green-100 text-green-800 px-2 py-1 rounded' : 'bg-red-100 text-red-800 px-2 py-1 rounded'">
                  {{ supplier.status }}
                </span>
              </td>
              <td>
                <!-- Thao tác (sửa/xem/xóa) giữ nguyên -->
                <button @click="editSupplier(supplier)" class="text-blue-600 hover:text-blue-800">Sửa</button>
                <button @click="viewSupplierDetails(supplier)" class="text-gray-600 hover:text-gray-800">Xem</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add Supplier Modal -->
    <div v-if="showAddSupplierModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Thêm nhà cung cấp mới</h3>
        
        <form @submit.prevent="addSupplier">
          <div class="space-y-4">
            <div>
              <label class="form-label">Tên nhà cung cấp</label>
              <input v-model="newSupplier.name" type="text" class="form-input" required>
            </div>
            
            <div>
              <label class="form-label">Mã nhà cung cấp</label>
              <input v-model="newSupplier.code" type="text" class="form-input" required>
            </div>
            
            <div>
              <label class="form-label">Số điện thoại</label>
              <input v-model="newSupplier.phone" type="tel" class="form-input" required>
            </div>
            
            <div>
              <label class="form-label">Email</label>
              <input v-model="newSupplier.email" type="email" class="form-input">
            </div>
            
            <div>
              <label class="form-label">Địa chỉ</label>
              <textarea v-model="newSupplier.address" class="form-input" rows="2"></textarea>
            </div>
            
            <div>
              <label class="form-label">Thành phố</label>
              <input v-model="newSupplier.city" type="text" class="form-input">
            </div>
          </div>
          
          <div class="flex justify-end space-x-3 mt-6">
            <button @click="showAddSupplierModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Thêm nhà cung cấp
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { suppliersAPI } from '@/services/suppliers'

export default {
  name: 'Suppliers',
  data() {
    return {
      searchTerm: '',
      statusFilter: '',
      showAddSupplierModal: false,
      suppliers: [],
      loading: false,
      totalSuppliers: 0,
      activeSuppliers: 0,
      totalValue: 0,
      monthlyOrders: 0
    }
  },
  mounted() {
    this.fetchSuppliers()
  },
  computed: {
    filteredSuppliers() {
      let filtered = this.suppliers;
      
      if (this.searchTerm) {
        filtered = filtered.filter(supplier => 
          supplier.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          supplier.code.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
          supplier.phone.includes(this.searchTerm) ||
          supplier.email.toLowerCase().includes(this.searchTerm.toLowerCase())
        );
      }
      
      if (this.statusFilter) {
        filtered = filtered.filter(supplier => supplier.status === this.statusFilter);
      }
      
      return filtered;
    }
  },
  methods: {
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN').format(value);
    },
    async fetchSuppliers() {
      this.loading = true
      try {
        const response = await suppliersAPI.getAll()
        // Nếu backend trả về dạng { content: [...], ... } (phân trang)
        this.suppliers = response.data.content || response.data
        this.totalSuppliers = response.data.totalElements || response.data.length
        this.activeSuppliers = response.data.activeSuppliers || 0
        this.totalValue = response.data.totalValue || 0
        this.monthlyOrders = response.data.monthlyOrders || 0
      } catch (error) {
        alert('Lỗi khi tải danh sách nhà cung cấp')
        this.suppliers = []
      } finally {
        this.loading = false
      }
    },
    addSupplier() {
      const newSupplier = {
        id: Date.now(),
        ...this.newSupplier
      };
      
      this.suppliers.push(newSupplier);
      this.showAddSupplierModal = false;
      
      // Reset form
      this.newSupplier = {
        name: '',
        code: '',
        phone: '',
        email: '',
        address: '',
        city: '',
        status: 'active',
        totalValue: 0
      };
      
      console.log('Đã thêm nhà cung cấp:', newSupplier);
    },
    editSupplier(supplier) {
      console.log('Chỉnh sửa nhà cung cấp:', supplier.name);
    },
    viewSupplierDetails(supplier) {
      console.log('Xem chi tiết nhà cung cấp:', supplier.name);
    }
  }
}
</script>

<style scoped>
.suppliers-page {
  max-width: 100%;
}

.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 24px;
}

.btn-primary {
  background-color: #0070F4;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
}

.btn-secondary:hover {
  background-color: #e5e7eb;
}

.form-input {
  display: block;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  border-color: #0070F4;
  box-shadow: 0 0 0 2px rgba(0, 112, 244, 0.1);
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 4px;
}
</style>
