<template>
  <div class="vehicles-container">
    <div class="header">
      <h2>Quản lý xe</h2>
      <button @click="showAddForm = true" class="btn-primary">Thêm xe mới</button>
    </div>

    <!-- Search and Filter -->
    <div class="search-filter">
      <input 
        v-model="searchTerm" 
        @input="searchVehicles"
        placeholder="Tìm kiếm xe..." 
        class="search-input"
      />
      <select v-model="selectedBrand" @change="filterVehicles" class="filter-select">
        <option value="">Tất cả hãng xe</option>
        <option v-for="brand in brands" :key="brand" :value="brand">{{ brand }}</option>
      </select>
    </div>

    <!-- Vehicles Table -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Hãng xe</th>
            <th>Model</th>
            <th>Năm sản xuất</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="vehicle in filteredVehicles" :key="vehicle.id">
            <td>{{ vehicle.id }}</td>
            <td>{{ vehicle.brand }}</td>
            <td>{{ vehicle.model }}</td>
            <td>{{ vehicle.year }}</td>
            <td>
              <button @click="editVehicle(vehicle)" class="btn-edit">Sửa</button>
              <button @click="deleteVehicle(vehicle.id)" class="btn-delete">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">Trước</button>
      <span>Trang {{ currentPage }} / {{ totalPages }}</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">Sau</button>
    </div>

    <!-- Add/Edit Modal -->
    <div v-if="showAddForm || showEditForm" class="modal-overlay">
      <div class="modal">
        <h3>{{ showEditForm ? 'Sửa xe' : 'Thêm xe mới' }}</h3>
        <form @submit.prevent="showEditForm ? updateVehicle() : addVehicle()">
          <div class="form-group">
            <label>Hãng xe:</label>
            <input v-model="formData.brand" required />
          </div>
          <div class="form-group">
            <label>Model:</label>
            <input v-model="formData.model" required />
          </div>
          <div class="form-group">
            <label>Năm sản xuất:</label>
            <input v-model="formData.year" type="number" min="1900" max="2030" required />
          </div>
          <div class="form-actions">
            <button type="submit" class="btn-primary">
              {{ showEditForm ? 'Cập nhật' : 'Thêm' }}
            </button>
            <button type="button" @click="closeForm" class="btn-secondary">Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { vehiclesApi } from '../api/vehicles.js'

export default {
  name: 'Vehicles',
  data() {
    return {
      vehicles: [],
      filteredVehicles: [],
      searchTerm: '',
      selectedBrand: '',
      currentPage: 1,
      pageSize: 10,
      totalPages: 1,
      showAddForm: false,
      showEditForm: false,
      formData: {
        brand: '',
        model: '',
        year: new Date().getFullYear()
      },
      editingId: null
    }
  },
  computed: {
    brands() {
      return [...new Set(this.vehicles.map(v => v.brand))]
    }
  },
  async mounted() {
    await this.loadVehicles()
  },
  methods: {
    async loadVehicles() {
      try {
        const response = await vehiclesApi.getAll()
        this.vehicles = response.data || []
        this.filteredVehicles = [...this.vehicles]
        this.updatePagination()
      } catch (error) {
        console.error('Lỗi khi tải danh sách xe:', error)
        // Fallback to mock data
        this.vehicles = [
          { id: 1, brand: 'Toyota', model: 'Hilux', year: 2020 },
          { id: 2, brand: 'Ford', model: 'Ranger', year: 2021 },
          { id: 3, brand: 'Isuzu', model: 'D-Max', year: 2019 }
        ]
        this.filteredVehicles = [...this.vehicles]
        this.updatePagination()
      }
    },
    searchVehicles() {
      this.filterVehicles()
    },
    filterVehicles() {
      let filtered = this.vehicles

      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase()
        filtered = filtered.filter(v => 
          v.brand.toLowerCase().includes(term) ||
          v.model.toLowerCase().includes(term) ||
          v.year.toString().includes(term)
        )
      }

      if (this.selectedBrand) {
        filtered = filtered.filter(v => v.brand === this.selectedBrand)
      }

      this.filteredVehicles = filtered
      this.currentPage = 1
      this.updatePagination()
    },
    updatePagination() {
      this.totalPages = Math.ceil(this.filteredVehicles.length / this.pageSize)
      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages || 1
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
      }
    },
    editVehicle(vehicle) {
      this.formData = { ...vehicle }
      this.editingId = vehicle.id
      this.showEditForm = true
    },
    async addVehicle() {
      try {
        await vehiclesApi.create(this.formData)
        await this.loadVehicles()
        this.closeForm()
      } catch (error) {
        console.error('Lỗi khi thêm xe:', error)
      }
    },
    async updateVehicle() {
      try {
        await vehiclesApi.update(this.editingId, this.formData)
        await this.loadVehicles()
        this.closeForm()
      } catch (error) {
        console.error('Lỗi khi cập nhật xe:', error)
      }
    },
    async deleteVehicle(id) {
      if (confirm('Bạn có chắc muốn xóa xe này?')) {
        try {
          await vehiclesApi.delete(id)
          await this.loadVehicles()
        } catch (error) {
          console.error('Lỗi khi xóa xe:', error)
        }
      }
    },
    closeForm() {
      this.showAddForm = false
      this.showEditForm = false
      this.formData = {
        brand: '',
        model: '',
        year: new Date().getFullYear()
      }
      this.editingId = null
    }
  }
}
</script>

<style scoped>
.vehicles-container {
  background: white;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.header h2 {
  color: #333;
  margin: 0;
}

.search-filter {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}

.search-input, .filter-select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex: 1;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1rem;
}

.data-table th,
.data-table td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1rem;
}

.pagination button {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  min-width: 400px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1rem;
}

.btn-primary {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-secondary {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-edit {
  background: #ffc107;
  color: #212529;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 0.5rem;
}

.btn-delete {
  background: #dc3545;
  color: white;
  border: none;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  cursor: pointer;
}
</style> 