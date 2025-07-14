<template>
  <div class="pricing-page">
    <!-- Header -->
    <div class="mb-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-2">Thiết lập giá</h1>
      <p class="text-gray-600">Quản lý giá bán và chính sách giá cho các sản phẩm</p>
    </div>

    <!-- Pricing Strategy Cards -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <div class="card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-semibold text-gray-900">Giá cơ bản</h3>
          <div class="h-8 w-8 bg-blue-100 rounded-full flex items-center justify-center">
            <svg class="h-4 w-4 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
            </svg>
          </div>
        </div>
        <p class="text-2xl font-bold text-gray-900">{{ activePriceRules.basic }}</p>
        <p class="text-sm text-gray-600">Giá bán tiêu chuẩn</p>
      </div>

      <div class="card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-semibold text-gray-900">Giá khuyến mãi</h3>
          <div class="h-8 w-8 bg-green-100 rounded-full flex items-center justify-center">
            <svg class="h-4 w-4 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
            </svg>
          </div>
        </div>
        <p class="text-2xl font-bold text-gray-900">{{ activePriceRules.promotion }}</p>
        <p class="text-sm text-gray-600">Giá khuyến mãi đang áp dụng</p>
      </div>

      <div class="card">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-semibold text-gray-900">Giá bán buôn</h3>
          <div class="h-8 w-8 bg-purple-100 rounded-full flex items-center justify-center">
            <svg class="h-4 w-4 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
          </div>
        </div>
        <p class="text-2xl font-bold text-gray-900">{{ activePriceRules.wholesale }}</p>
        <p class="text-sm text-gray-600">Giá bán buôn (từ 10 sản phẩm)</p>
      </div>
    </div>

    <!-- Price Rules Management -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
      <!-- Price Rules List -->
      <div class="card">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Quy tắc giá</h2>
          <button @click="showCreateRuleModal = true" class="btn-primary">
            <svg class="mr-2 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Tạo quy tắc
          </button>
        </div>

        <div class="space-y-4">
          <div v-for="rule in priceRules" :key="rule.id" class="border rounded-lg p-4">
            <div class="flex items-center justify-between mb-2">
              <h3 class="font-medium text-gray-900">{{ rule.name }}</h3>
              <div class="flex items-center space-x-2">
                <span class="px-2 py-1 text-xs font-medium rounded-full" 
                      :class="rule.active ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'">
                  {{ rule.active ? 'Hoạt động' : 'Tạm dừng' }}
                </span>
                <button @click="toggleRule(rule)" class="text-blue-600 hover:text-blue-800">
                  <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                  </svg>
                </button>
              </div>
            </div>
            <p class="text-sm text-gray-600 mb-2">{{ rule.description }}</p>
            <div class="flex items-center justify-between">
              <span class="text-sm font-medium text-gray-700">{{ rule.type }}</span>
              <span class="text-sm font-bold text-[#0070F4]">{{ rule.discount }}% giảm</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Product Pricing -->
      <div class="card">
        <div class="flex items-center justify-between mb-6">
          <h2 class="text-xl font-semibold text-gray-900">Giá theo sản phẩm</h2>
          <div class="flex items-center space-x-2">
            <input v-model="searchProduct" type="text" placeholder="Tìm sản phẩm..." 
                   class="form-input w-48">
            <button @click="refreshPricing" class="btn-secondary">
              <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
            </button>
          </div>
        </div>

        <div class="max-h-96 overflow-y-auto">
          <div class="space-y-3">
            <div v-for="product in filteredProducts" :key="product.id" class="border rounded-lg p-4">
              <div class="flex items-center justify-between mb-3">
                <div>
                  <h3 class="font-medium text-gray-900">{{ product.name }}</h3>
                  <p class="text-sm text-gray-600">{{ product.category }}</p>
                </div>
                <button @click="editProductPricing(product)" class="text-blue-600 hover:text-blue-800">
                  <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
              </div>
              <div class="grid grid-cols-3 gap-3 text-sm">
                <div>
                  <span class="text-gray-600">Giá gốc:</span>
                  <p class="font-medium">₫{{ formatCurrency(product.basePrice) }}</p>
                </div>
                <div>
                  <span class="text-gray-600">Giá bán:</span>
                  <p class="font-medium text-[#0070F4]">₫{{ formatCurrency(product.sellPrice) }}</p>
                </div>
                <div>
                  <span class="text-gray-600">Lợi nhuận:</span>
                  <p class="font-medium text-green-600">{{ calculateProfit(product) }}%</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Create Rule Modal -->
    <div v-if="showCreateRuleModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-md">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Tạo quy tắc giá mới</h3>
        
        <form @submit.prevent="createPriceRule">
          <div class="mb-4">
            <label class="form-label">Tên quy tắc</label>
            <input v-model="newRule.name" type="text" class="form-input" required>
          </div>
          
          <div class="mb-4">
            <label class="form-label">Mô tả</label>
            <textarea v-model="newRule.description" class="form-input" rows="2"></textarea>
          </div>
          
          <div class="mb-4">
            <label class="form-label">Loại quy tắc</label>
            <select v-model="newRule.type" class="form-input">
              <option value="percentage">Giảm theo %</option>
              <option value="fixed">Giảm số tiền cố định</option>
              <option value="bulk">Giảm giá theo số lượng</option>
            </select>
          </div>
          
          <div class="mb-4">
            <label class="form-label">Giá trị giảm</label>
            <input v-model="newRule.discount" type="number" class="form-input" min="0" required>
          </div>
          
          <div class="mb-6">
            <label class="flex items-center">
              <input v-model="newRule.active" type="checkbox" class="mr-2">
              <span class="text-sm text-gray-700">Kích hoạt ngay</span>
            </label>
          </div>
          
          <div class="flex justify-end space-x-3">
            <button @click="showCreateRuleModal = false" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              Tạo quy tắc
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Pricing',
  data() {
    return {
      searchProduct: '',
      showCreateRuleModal: false,
      activePriceRules: {
        basic: 156,
        promotion: 23,
        wholesale: 8
      },
      priceRules: [
        {
          id: 1,
          name: 'Khuyến mãi cuối tuần',
          description: 'Giảm giá 10% cho tất cả sản phẩm vào cuối tuần',
          type: 'Giảm theo %',
          discount: 10,
          active: true
        },
        {
          id: 2,
          name: 'Giá bán buôn',
          description: 'Giảm giá 15% khi mua từ 10 sản phẩm trở lên',
          type: 'Giảm theo số lượng',
          discount: 15,
          active: true
        },
        {
          id: 3,
          name: 'Khách hàng VIP',
          description: 'Giảm giá 5% cho khách hàng VIP',
          type: 'Giảm theo %',
          discount: 5,
          active: false
        }
      ],
      products: [
        {
          id: 1,
          name: 'Phanh đĩa Hyundai',
          category: 'Hệ thống phanh',
          basePrice: 750000,
          sellPrice: 850000,
          stock: 15
        },
        {
          id: 2,
          name: 'Lọc dầu động cơ',
          category: 'Hệ thống lọc',
          basePrice: 200000,
          sellPrice: 250000,
          stock: 32
        },
        {
          id: 3,
          name: 'Bình acquy 12V',
          category: 'Điện xe',
          basePrice: 1000000,
          sellPrice: 1200000,
          stock: 8
        },
        {
          id: 4,
          name: 'Dây curoa',
          category: 'Truyền động',
          basePrice: 150000,
          sellPrice: 180000,
          stock: 25
        },
        {
          id: 5,
          name: 'Lốp xe tải 825R16',
          category: 'Lốp xe',
          basePrice: 2200000,
          sellPrice: 2500000,
          stock: 12
        },
        {
          id: 6,
          name: 'Đèn pha LED',
          category: 'Đèn xe',
          basePrice: 350000,
          sellPrice: 450000,
          stock: 18
        }
      ],
      newRule: {
        name: '',
        description: '',
        type: 'percentage',
        discount: 0,
        active: true
      }
    }
  },
  computed: {
    filteredProducts() {
      if (!this.searchProduct) return this.products;
      return this.products.filter(product => 
        product.name.toLowerCase().includes(this.searchProduct.toLowerCase()) ||
        product.category.toLowerCase().includes(this.searchProduct.toLowerCase())
      );
    }
  },
  methods: {
    formatCurrency(value) {
      return new Intl.NumberFormat('vi-VN').format(value);
    },
    calculateProfit(product) {
      const profit = ((product.sellPrice - product.basePrice) / product.basePrice) * 100;
      return Math.round(profit);
    },
    toggleRule(rule) {
      rule.active = !rule.active;
      this.$nextTick(() => {
        // Simulate API call
        console.log(`Quy tắc "${rule.name}" đã được ${rule.active ? 'kích hoạt' : 'tạm dừng'}`);
      });
    },
    createPriceRule() {
      const newRule = {
        id: Date.now(),
        ...this.newRule
      };
      
      this.priceRules.push(newRule);
      this.showCreateRuleModal = false;
      
      // Reset form
      this.newRule = {
        name: '',
        description: '',
        type: 'percentage',
        discount: 0,
        active: true
      };
      
      console.log('Đã tạo quy tắc giá mới:', newRule);
    },
    editProductPricing(product) {
      // Simulate opening product pricing modal
      console.log('Chỉnh sửa giá cho sản phẩm:', product.name);
    },
    refreshPricing() {
      // Simulate refreshing pricing data
      console.log('Đang làm mới dữ liệu giá...');
    }
  }
}
</script>

<style scoped>
.pricing-page {
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

.transition-all {
  transition: all 0.3s ease;
}

/* Custom scrollbar */
.max-h-96::-webkit-scrollbar {
  width: 6px;
}

.max-h-96::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.max-h-96::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.max-h-96::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
