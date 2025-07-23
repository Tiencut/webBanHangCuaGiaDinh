<template>
  <div>
    <div
      class="category-row"
      :data-parent-id="category.id"
      :data-category-id="category.id"
    >
      <div class="flex items-center">
        <!-- Drag Handle -->
        <div class="drag-handle cursor-move pr-2 text-gray-400 hover:text-gray-600">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7"></path>
          </svg>
        </div>

        <div 
          class="flex-1 flex items-center cursor-pointer"
          :style="{ 'padding-left': `${level * 20}px` }"
          @click="$emit('toggle')"
        >
          <button
            class="mr-2 p-1 rounded-full hover:bg-gray-200 transition-colors"
          >
            <svg
              v-if="hasChildren"
              class="w-4 h-4 transform transition-transform"
              :class="{ 'rotate-90': expanded }"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 5l7 7-7 7"
              ></path>
            </svg>
            <div v-else class="w-4 h-4"></div> <!-- Placeholder to keep alignment -->
          </button>
          <div class="font-medium text-gray-800">{{ category.name }}</div>
        </div>
        
        <div class="w-40 text-sm text-gray-600">{{ category.code }}</div>
        <div class="w-32 text-center">
          <span class="px-2 py-1 text-xs rounded-full font-medium"
                :class="category.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'">
            {{ category.isActive ? 'Hoạt động' : 'Không hoạt động' }}
          </span>
        </div>
        <div class="w-24 text-center text-sm font-medium text-blue-600">{{ category.productCount || 0 }}</div>
        <div class="w-24 flex items-center justify-center space-x-2">
          <button
            @click="$emit('add', category)"
            class="text-green-600 hover:text-green-900 transition-colors"
            title="Thêm danh mục con"
          >
             <svg class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path></svg>
          </button>
          <button
            @click="$emit('edit', category)"
            class="text-blue-600 hover:text-blue-900 transition-colors"
            title="Chỉnh sửa"
          >
             <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
          </button>
          <button
            @click="$emit('delete', category)"
            class="text-red-600 hover:text-red-900 transition-colors"
            title="Xóa"
          >
             <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
              </svg>
          </button>
        </div>
      </div>
    </div>

    <draggable
      v-if="expanded && hasChildren"
      :list="category.children"
      @end="$emit('end', $event)"
      item-key="id"
      group="categories"
      handle=".drag-handle"
      class="pl-6"
    >
      <template #item="{ element: child }">
        <CategoryTreeRow
          :category="child"
          :level="level + 1"
          :expanded="child.expanded"
          @toggle="$emit('toggle-child', child.id)"
          @add="$emit('add', $event)"
          @edit="$emit('edit', $event)"
          @delete="$emit('delete', $event)"
          @toggle-child="(id) => $emit('toggle-child', id)"
          @end="$emit('end', $event)"
          @update:list="newChildren => $emit('update:children', newChildren)"
        />
      </template>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable';

export default {
  name: 'CategoryTreeRow',
  components: {
    draggable,
  },
  props: {
    category: {
      type: Object,
      required: true,
    },
    level: {
      type: Number,
      default: 0,
    },
    expanded: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['toggle', 'add', 'edit', 'delete', 'toggle-child', 'end', 'update:children'],
  computed: {
    hasChildren() {
      return this.category.children && this.category.children.length > 0;
    },
  },
};
</script>

<style scoped>
.category-row {
  border-bottom: 1px solid #e5e7eb;
  padding-top: 0.75rem;
  padding-bottom: 0.75rem;
  background-color: white;
}
.category-row:hover {
  background-color: #f9fafb;
}
</style> 