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
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nhà cung cấp
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Liên hệ
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Địa chỉ
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Trạng thái
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Tổng giá trị
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Thao tác
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="supplier in filteredSuppliers" :key="supplier.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div class="h-10 w-10 bg-gray-200 rounded-full flex items-center justify-center">
                    <svg class="h-5 w-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                    </svg>
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">{{ supplier.name }}</div>
                    <div class="text-sm text-gray-500">{{ supplier.code }}</div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ supplier.phone }}</div>
                <div class="text-sm text-gray-500">{{ supplier.email }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ supplier.address }}</div>
                <div class="text-sm text-gray-500">{{ supplier.city }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="px-2 py-1 text-xs font-medium rounded-full"
                      :class="supplier.status === 'active' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
                  {{ supplier.status === 'active' ? 'Hoạt động' : 'Tạm dừng' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                ₫{{ formatCurrency(supplier.totalValue) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <div class="flex items-center space-x-2">
                  <button @click="editSupplier(supplier)" class="text-blue-600 hover:text-blue-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                    </svg>
                  </button>
                  <button @click="viewSupplierDetails(supplier)" class="text-gray-600 hover:text-gray-800">
                    <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                    </svg>
                  </button>
                </div>
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
export default {
  name: 'Suppliers',
  data() {
    return {
      searchTerm: '',
      statusFilter: '',
      showAddSupplierModal: false,
      totalSuppliers: 24,
      activeSuppliers: 18,
      totalValue: 450000000,
      monthlyOrders: 67,
      suppliers: [
        {
          id: 1,
          name: 'Công ty TNHH Phụ tùng Hà Nội',
          code: 'SUP001',
          phone: '024-3456-7890',
          email: 'info@phutunghanoi.com',
          address: '123 Đường Láng, Đống Đa',
          city: 'Hà Nội',
          status: 'active',
          totalValue: 85000000
        },
        {
          id: 2,
          name: 'Công ty Cổ phần Ô tô Thành Công',
          code: 'SUP002',
          phone: '028-9876-5432',
          email: 'sales@thanhcong.com',
          address: '456 Nguyễn Văn Linh, Quận 7',
          city: 'TP. Hồ Chí Minh',
          status: 'active',
          totalValue: 120000000
        },
        {
          id: 3,
          name: 'Cửa hàng Phụ tùng Minh Tuấn',
          code: 'SUP003',
          phone: '0236-123-4567',
          email: 'minhtuan@gmail.com',
          address: '789 Lê Duẩn, Hải Châu',
          city: 'Đà Nẵng',
          status: 'inactive',
          totalValue: 45000000
        },
        {
          id: 4,
          name: 'Công ty TNHH Phụ tùng Việt Nam',
          code: 'SUP004',
          phone: '0225-789-0123',
          email: 'info@phutungvn.com',
          address: '321 Trần Phú, Hồng Bàng',
          city: 'Hải Phòng',
          status: 'active',
          totalValue: 75000000
        },
        {
          id: 5,
          name: 'Cửa hàng Phụ tùng Đại Phát',
          code: 'SUP005',
          phone: '0292-654-3210',
          email: 'daiphat@yahoo.com',
          address: '654 Nguyễn Văn Cừ, Cái Răng',
          city: 'Cần Thơ',
          status: 'active',
          totalValue: 62000000
        }
      ],
      newSupplier: {
        name: '',
        code: '',
        phone: '',
        email: '',
        address: '',
        city: '',
        status: 'active',
        totalValue: 0
      }
    }
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
