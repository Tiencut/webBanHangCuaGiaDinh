<template>
  <div class="w-full min-h-screen bg-gray-50">
    <div class="px-6 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Quản lý danh mục</h1>
        <p class="text-gray-600 mt-1">Kéo và thả để sắp xếp lại các danh mục.</p>
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
        <div class="w-56 text-sm text-gray-600">Cách bán</div>
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
      <draggable
        v-else
        v-model="categoryTree"
        @end="handleDragEnd"
        item-key="id"
        group="categories"
        handle=".drag-handle"
        class="w-full"
      >
        <template #item="{ element: rootCategory }">
          <CategoryTreeRow
            :category="rootCategory"
            :level="0"
            :expanded="expandedState[rootCategory.id]"
            @toggle="toggleNode(rootCategory.id)"
            @add="handleAddChild"
            @edit="handleEdit"
            @delete="handleDelete"
            @toggle-child="toggleNode"
            @update:children="newChildren => handleChildrenUpdate(rootCategory, newChildren)"
          />
        </template>
      </draggable>
    </div>
    <!-- Kết thúc -->

    <!-- Add/Edit Category Modal (extracted) -->
    <CategoryFormModal
      :visible="showModal"
      @update:visible="value => showModal = value"
      :selected="selectedCategory"
      :parentId="categoryForm.parentId"
      :flatCategories="flatCategories"
      @saved="loadCategories"
    />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import draggable from 'vuedraggable'
import CategoryTreeRow from '@/components/CategoryTreeRow.vue'
import CategoryFormModal from '@/components/category/CategoryFormModal.vue'
import { categoriesApi } from '@/api/categories.js'

// helper: add updateCategoryParent if not present
if (!categoriesApi.updateCategoryParent) {
  categoriesApi.updateCategoryParent = (categoryId, newParentId) => {
    return categoriesApi.put(`/${categoryId}/parent`, { newParentId });
  };
}

const loading = ref(false)
const showModal = ref(false)
const isEditing = ref(false)
const selectedCategory = ref(null)
const categories = ref([])
const expandedState = ref({})

const categoryForm = ref({
  name: '',
  code: '',
  description: '',
  sellingMethodsRaw: '',
  sellingMethods: [],
  parentId: null,
  sortOrder: 0,
  isActive: true
})

const buildTree = (list) => {
  const map = {}
  list.forEach(item => { map[item.id] = { ...item, children: [] } })
  const roots = []
  Object.values(map).forEach(node => {
    if (node.parentId == null) roots.push(node)
    else if (map[node.parentId]) map[node.parentId].children.push(node)
    else roots.push(node)
  })
  return roots
}

const buildFlatList = (list) => {
  const roots = buildTree(list)
  const res = []
  const walk = (nodes, level) => {
    nodes.forEach(n => {
      res.push({ id: n.id, name: n.name, level })
      if (n.children && n.children.length) walk(n.children, level + 1)
    })
  }
  walk(roots, 0)
  return res
}

// Use a writable tree for draggable. Keep it in sync with `categories`.
const categoryTree = ref(buildTree(categories.value))
watch(categories, (newVal) => {
  categoryTree.value = buildTree(newVal)
}, { deep: true })
const flatCategories = computed(() => buildFlatList(categories.value))

const toggleNode = (id) => { expandedState.value[id] = !expandedState.value[id] }

const resetForm = () => {
  categoryForm.value = {
    name: '',
    code: '',
    description: '',
    sellingMethodsRaw: '',
    sellingMethods: [],
    parentId: null,
    sortOrder: 0,
    isActive: true
  }
}

const handleCreateRoot = () => { resetForm(); isEditing.value = false; selectedCategory.value = null; showModal.value = true }

const handleAddChild = (parentCategory) => { resetForm(); categoryForm.value.parentId = parentCategory.id; isEditing.value = false; showModal.value = true }

const handleEdit = (category) => {
  isEditing.value = true
  selectedCategory.value = category
  categoryForm.value = {
    name: category.name,
    code: category.code,
    description: category.description,
    sellingMethodsRaw: (category.sellingMethods && category.sellingMethods.length) ? category.sellingMethods.join('\n') : '',
    sellingMethods: category.sellingMethods || [],
    parentId: category.parentId || null,
    sortOrder: category.sortOrder || 0,
    isActive: category.isActive !== false
  }
  showModal.value = true
}

const handleDelete = async (category) => {
  if (!confirm(`Bạn có chắc chắn muốn xóa danh mục "${category.name}"?`)) return
  try {
    await categoriesApi.deleteCategory(category.id)
    await loadCategories()
  } catch (error) {
    console.error('Error deleting category:', error)
    alert('Xóa danh mục thất bại. Vui lòng thử lại.')
  }
}

const findCategoryById = (nodes, id) => {
  for (const node of nodes) {
    if (node.id === id) return node
    if (node.children) {
      const found = findCategoryById(node.children, id)
      if (found) return found
    }
  }
  return null
}

const handleChildrenUpdate = (parentCategory, newChildren) => { parentCategory.children = newChildren }

const handleDragEnd = async (event) => {
  try {
    const draggedEl = event.item
    const draggedCategoryId = draggedEl && draggedEl.dataset ? parseInt(draggedEl.dataset.categoryId) : null
    const to = event.to

    // Walk up from the drop container to find an element with data-category-id or data-parent-id
    const findParentIdFromContainer = (el) => {
      let node = el
      while (node) {
        if (node.dataset && node.dataset.parentId) return parseInt(node.dataset.parentId)
        if (node.dataset && node.dataset.categoryId) return parseInt(node.dataset.categoryId)
        node = node.parentElement
      }
      return null
    }

    const newParentId = findParentIdFromContainer(to)
    if (!draggedCategoryId) return

    const originalCategory = findCategoryById(categories.value, draggedCategoryId)
    if (originalCategory && originalCategory.parentId === newParentId) return

    await categoriesApi.updateCategoryParent(draggedCategoryId, newParentId)
    await loadCategories()
  } catch (error) {
    console.error('Failed to update category parent:', error)
    alert('Cập nhật thứ tự danh mục thất bại! Vui lòng thử lại.')
    await loadCategories()
  }
}

const loadCategories = async () => {
  try {
    loading.value = true
    const response = await categoriesApi.getAllCategories()
    categories.value = response.data || []
    const newExpanded = {}
    categories.value.forEach(cat => {
      if (categories.value.some(c => c.parentId === cat.id)) newExpanded[cat.id] = true
    })
    expandedState.value = newExpanded
  } catch (error) {
    console.error('Error loading categories:', error)
    categories.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadCategories)
</script>