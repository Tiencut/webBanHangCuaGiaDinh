<template>
  <div v-if="visible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
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
            <input v-model="form.name" type="text" class="form-input w-full" required>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Mã danh mục</label>
            <input v-model="form.code" type="text" class="form-input w-full" placeholder="ENGINE, BRAKE, TIRE...">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Danh mục cha</label>
            <select v-model="form.parentId" class="form-input w-full">
              <option :value="null">Không có (danh mục gốc)</option>
              <option v-for="cat in flatCategories" :key="cat.id" :value="cat.id">
                <span v-for="i in cat.level" :key="i">&nbsp;&nbsp;</span>
                {{ cat.name }}
              </option>
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Thứ tự sắp xếp</label>
            <input v-model.number="form.sortOrder" type="number" min="0" class="form-input w-full">
          </div>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Mô tả</label>
          <textarea v-model="form.description" rows="3" class="form-input w-full" placeholder="Mô tả chi tiết về danh mục này..."></textarea>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Cách bán (mỗi dòng 1 cách)</label>
          <textarea v-model="form.sellingMethodsRaw" rows="3" class="form-input w-full"
                    placeholder="Ví dụ:\nCái\nMét\nBộ\nGhi chú... (mỗi dòng 1 cách bán)"></textarea>
          <p class="text-xs text-gray-500 mt-1">Nhập mỗi cách bán trên một dòng. API sẽ lưu dưới dạng mảng JSON.</p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Trạng thái</label>
          <select v-model="form.isActive" class="form-input w-full">
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
</template>

<script>
import { ref, watch, computed, onMounted, onBeforeUnmount } from 'vue'
import { categoriesApi } from '@/api/categories.js'

export default {
  name: 'CategoryFormModal',
  props: {
    visible: { type: Boolean, default: false },
    selected: { type: Object, default: null },
    parentId: { type: [Number, null], default: null },
    flatCategories: { type: Array, default: () => [] }
  },
  emits: ['update:visible', 'saved'],
  setup(props, { emit }) {
    const form = ref({
      name: '',
      code: '',
      description: '',
      sellingMethodsRaw: '',
      sellingMethods: [],
      parentId: null,
      sortOrder: 0,
      isActive: true
    })

    const isEditing = computed(() => !!props.selected)

    const resetForm = () => {
      form.value = {
        name: '', code: '', description: '', sellingMethodsRaw: '', sellingMethods: [], parentId: props.parentId || null, sortOrder: 0, isActive: true
      }
    }

    watch(() => props.visible, (v) => {
      if (v) {
        if (props.selected) {
          form.value = {
            name: props.selected.name || '',
            code: props.selected.code || '',
            description: props.selected.description || '',
            sellingMethodsRaw: (props.selected.sellingMethods && props.selected.sellingMethods.length) ? props.selected.sellingMethods.join('\n') : '',
            sellingMethods: props.selected.sellingMethods || [],
            parentId: props.selected.parentId || null,
            sortOrder: props.selected.sortOrder || 0,
            isActive: props.selected.isActive !== undefined ? props.selected.isActive : true
          }
        } else {
          form.value.parentId = props.parentId || null
        }
      }
    })

    const closeModal = () => {
      emit('update:visible', false)
      resetForm()
    }

    // Close on Escape key
    const handleKeydown = (e) => {
      if (e.key === 'Escape' || e.key === 'Esc') {
        if (props.visible) closeModal()
      }
    }

    onMounted(() => {
      window.addEventListener('keydown', handleKeydown)
    })

    onBeforeUnmount(() => {
      window.removeEventListener('keydown', handleKeydown)
    })

    const saveCategory = async () => {
      try {
        const raw = form.value.sellingMethodsRaw || ''
        const arr = raw.split(/\r?\n/).map(s => s.trim()).filter(Boolean)
        form.value.sellingMethods = arr

        if (isEditing.value) {
          await categoriesApi.updateCategory(props.selected.id, form.value)
        } else {
          await categoriesApi.createCategory(form.value)
        }

        emit('saved')
        emit('update:visible', false)
        resetForm()
      } catch (err) {
          console.error('Error saving category:', err)
          // Show server-provided error body if available to help debugging
          if (err && err.response && err.response.data) {
            console.error('Server response:', err.response.data)
            alert('Lưu danh mục thất bại: ' + (err.response.data.message || JSON.stringify(err.response.data)))
          } else {
            alert('Lưu danh mục thất bại. Vui lòng thử lại.')
          }
      }
    }

    return { form, isEditing, closeModal, saveCategory }
  }
}
</script>

<style scoped>
.form-input { padding: 0.5rem 0.75rem; border: 1px solid #e5e7eb; border-radius: 0.375rem }
.btn-primary { background-color:#2563eb; color:white; padding:0.5rem 0.75rem; border-radius:0.375rem }
.btn-secondary { background-color:#f3f4f6; color:#111827; padding:0.5rem 0.75rem; border-radius:0.375rem }
</style>
