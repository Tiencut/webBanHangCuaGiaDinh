<template>
  <div class="w-full min-h-screen bg-gray-50">
    <div class="px-6 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Quản lý danh mục</h1>
        <p class="text-gray-600 mt-1">Quản lý danh mục sản phẩm theo cấu trúc cây</p>
      </div>
      <button @click="handleCreateRoot" class="btn-primary">
        Thêm danh mục gốc
      </button>
    </div>

    <!-- Category Tree View -->
    <div class="bg-white rounded-lg shadow-sm border border-gray-200">
      <!-- Tree Header -->
      <div class="flex items-center px-4 py-3 bg-gray-50 border-b border-gray-200 font-semibold text-sm text-gray-600">
        <div class="flex-1 pl-7">Tên danh mục</div>
        <div class="w-40">Mã</div>
        <div class="w-32 text-center">Trạng thái</div>
        <div class="w-24 text-center">Số SP</div>
        <div class="w-24 text-center">Thao tác</div>
      </div>
      
      <!-- Tree Body -->
      <div v-if="loading" class="p-8 text-center text-gray-500">
        Đang tải dữ liệu...
      </div>
      <div v-else-if="categoryTree.length === 0" class="p-8 text-center text-gray-500">
        Không có danh mục nào.
      </div>
      <div v-else>
        <CategoryTreeRow
          v-for="rootCategory in categoryTree"
          :key="rootCategory.id"
          :category="rootCategory"
          :level="0"
          :expanded="expandedState[rootCategory.id]"
          @toggle="toggleNode(rootCategory.id)"
          @add="handleAddChild"
          @edit="handleEdit"
          @delete="handleDelete"
          @toggle-child="toggleNode"
        />
      </div>
    </div>

    <!-- Add/Edit Category Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg p-6 w-full max-w-2xl max-h-[80vh] overflow-y-auto">
        <div class="flex justify-between items-center mb-4">
          <h3 class="text-lg font-semibold">{{ isEditing ? 'Chỉnh sửa danh mục' : 'Thêm danh mục mới' }}</h3>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="saveCategory" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên danh mục *</label>
              <input v-model="categoryForm.name" type="text" class="form-input w-full" required>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Mã danh mục</label>
              <input v-model="categoryForm.code" type="text" class="form-input w-full" placeholder="ENGINE, BRAKE, TIRE...">
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục cha</label>
              <select v-model="categoryForm.parentId" class="form-input w-full">
                <option :value="null">Không có (danh mục gốc)</option>
                <option v-for="cat in flatCategories" :key="cat.id" :value="cat.id">
                  <span v-for="i in cat.level" :key="i">&nbsp;&nbsp;</span>
                  {{ cat.name }}
                </option>
              </select>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Thứ tự sắp xếp</label>
              <input v-model.number="categoryForm.sortOrder" type="number" min="0" class="form-input w-full">
            </div>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
            <textarea v-model="categoryForm.description" rows="3" class="form-input w-full" 
                      placeholder="Mô tả chi tiết về danh mục này..."></textarea>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
            <select v-model="categoryForm.isActive" class="form-input w-full">
              <option :value="true">Hoạt động</option>
              <option :value="false">Không hoạt động</option>
            </select>
          </div>
          
          <div class="flex justify-end space-x-3 pt-4">
            <button @click="closeModal" type="button" class="btn-secondary">
              Hủy
            </button>
            <button type="submit" class="btn-primary">
              {{ isEditing ? 'Cập nhật' : 'Thêm danh mục' }}
            </button>
          </div>
        </form>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import CategoryTreeRow from '@/components/CategoryTreeRow.vue'
import { categoriesApi } from '@/api/categories.js'

export default {
  name: 'Categories',
  components: {
    CategoryTreeRow
  },
  setup() {
    const loading = ref(false)
    const showModal = ref(false)
    const isEditing = ref(false)
    const selectedCategory = ref(null)
    const categories = ref([])
    const expandedState = ref({});

    // Form data
    const categoryForm = ref({
      name: '',
      code: '',
      description: '',
      parentId: null,
      sortOrder: 0,
      isActive: true
    })

    const categoryTree = computed(() => {
      const allNodes = categories.value.map(cat => ({ ...cat, children: [], expanded: expandedState.value[cat.id] || false }));
      const tree = [];
      const map = allNodes.reduce((acc, node) => {
        acc[node.id] = node;
        return acc;
      }, {});

      allNodes.forEach(node => {
        if (node.parentId && map[node.parentId]) {
          map[node.parentId].children.push(node);
        } else {
          tree.push(node);
        }
      });
      return tree;
    });

    const flatCategories = computed(() => {
      const result = [];
      function flatten(nodes, level = 0) {
        nodes.forEach(node => {
          result.push({ ...node, level });
          if (node.children) {
            flatten(node.children, level + 1);
          }
        });
      }
      flatten(categoryTree.value);
      return result;
    });

    const toggleNode = (categoryId) => {
      expandedState.value[categoryId] = !expandedState.value[categoryId];
    };

    // Methods
    const handleCreateRoot = () => {
      isEditing.value = false
      selectedCategory.value = null
      resetForm()
      categoryForm.value.parentId = null // Ensure it's a root category
      showModal.value = true
    }

    const handleAddChild = (parentCategory) => {
      isEditing.value = false
      selectedCategory.value = null
      resetForm()
      categoryForm.value.parentId = parentCategory.id // Set parent
      showModal.value = true
    }

    const handleEdit = (category) => {
      isEditing.value = true
      selectedCategory.value = category
      categoryForm.value = {
        name: category.name,
        code: category.code,
        description: category.description,
        parentId: category.parentId || null,
        sortOrder: category.sortOrder,
        isActive: category.isActive
      }
      showModal.value = true
    }

    const handleDelete = async (category) => {
       if (confirm(`Bạn có chắc chắn muốn xóa danh mục "${category.name}"?`)) {
        try {
          await categoriesApi.deleteCategory(category.id)
          await loadCategories() // Reload categories
        } catch (error) {
          console.error('Error deleting category:', error)
          alert('Xóa danh mục thất bại. Vui lòng thử lại.');
        }
      }
    }

    const saveCategory = async () => {
      try {
        if (isEditing.value) {
          await categoriesApi.updateCategory(selectedCategory.value.id, categoryForm.value)
        } else {
          await categoriesApi.createCategory(categoryForm.value)
        }
        await loadCategories()
        closeModal()
      } catch (error) {
        console.error('Error saving category:', error)
        alert('Lưu danh mục thất bại. Vui lòng thử lại.');
      }
    }

    const closeModal = () => {
      showModal.value = false
      resetForm()
    }

    const resetForm = () => {
      categoryForm.value = {
        name: '',
        code: '',
        description: '',
        parentId: null,
        sortOrder: 0,
        isActive: true
      }
    }

    const loadCategories = async () => {
      try {
        loading.value = true
        const response = await categoriesApi.getAllCategories()
        categories.value = response.data

        // Expand all nodes by default
        const newExpandedState = {}
        response.data.forEach(cat => {
          if (response.data.some(c => c.parentId === cat.id)) {
            newExpandedState[cat.id] = true;
          }
        });
        expandedState.value = newExpandedState;

      } catch (error) {
        console.error('Error loading categories:', error)
        categories.value = []; // Reset on error
      } finally {
        loading.value = false
      }
    }
    
    onMounted(loadCategories);

    return {
      loading,
      showModal,
      isEditing,
      selectedCategory,
      categories,
      categoryTree,
      flatCategories,
      categoryForm,
      expandedState,
      handleCreateRoot,
      handleAddChild,
      handleEdit,
      handleDelete,
      saveCategory,
      closeModal,
      toggleNode,
    }
  }
}
</script> 