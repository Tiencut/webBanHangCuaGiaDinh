<template>
  <el-dialog
    v-model="dialogVisible"
    title="Chọn cột hiển thị"
    width="500px"
    :before-close="handleClose"
  >
    <el-checkbox-group v-model="tempColumnVisibility">
      <el-checkbox
        v-for="column in allColumns"
        :key="column.key"
        :label="column.key"
      >
        {{ column.label }}
      </el-checkbox>
    </el-checkbox-group>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="resetColumnVisibility">Đặt lại</el-button>
        <el-button @click="handleClose">Hủy</el-button>
        <el-button type="primary" @click="applyColumnVisibility">Áp dụng</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch, computed } from 'vue'
import { ElDialog, ElCheckboxGroup, ElCheckbox, ElButton } from 'element-plus'

const props = defineProps({
  visibleColumns: {
    type: Object,
    required: true
  },
  allColumns: {
    type: Array,
    required: true
  },
  showColumnSelector: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits([
  'open-column-selector',
  'reset-column-visibility',
  'update:showColumnSelector',
  'apply-column-visibility'
])

const dialogVisible = ref(props.showColumnSelector)

watch(() => props.showColumnSelector, (newVal) => {
  dialogVisible.value = newVal
})

watch(dialogVisible, (newVal) => {
  emit('update:showColumnSelector', newVal)
})

const tempColumnVisibility = ref({})

watch(() => props.visibleColumns, (newVal) => {
  tempColumnVisibility.value = { ...newVal }
}, { immediate: true })

const handleClose = () => {
  dialogVisible.value = false
}

const applyColumnVisibility = () => {
  emit('apply-column-visibility', tempColumnVisibility.value)
  dialogVisible.value = false
}

const resetColumnVisibility = () => {
  emit('reset-column-visibility')
  dialogVisible.value = false
}

// Add a simple filter/query and filteredColumns to match the products-scoped control
const filterQuery = ref('')
const filteredColumns = computed(() => {
  const q = String(filterQuery.value || '').trim().toLowerCase()
  if (!q) return props.allColumns
  return props.allColumns.filter(c => (c.label || c.key).toLowerCase().includes(q))
})
</script>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>