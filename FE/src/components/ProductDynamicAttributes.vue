<template>
  <div>
    <div v-if="loading" class="text-center py-8 text-gray-500">Đang tải thông số kỹ thuật...</div>
    <div v-else>
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Thuộc tính</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Giá trị</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">Đơn vị</th>
            <th class="px-3 py-2 text-xs font-semibold text-gray-600">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="attr in attributes" :key="attr.id" class="hover:bg-gray-50">
            <td class="px-3 py-2 text-sm">{{ attr.name }}</td>
            <td class="px-3 py-2 text-sm">
              <template v-if="editId === attr.id">
                <input
                  v-if="attr.type === 'TEXT'"
                  v-model="editValue"
                  type="text"
                  class="form-input w-full text-sm"
                  @keyup.enter="save(attr)"
                  @blur="save(attr)"
                  autofocus
                />
                <input
                  v-else-if="attr.type === 'NUMBER'"
                  v-model.number="editValue"
                  type="number"
                  class="form-input w-full text-sm"
                  @keyup.enter="save(attr)"
                  @blur="save(attr)"
                  autofocus
                />
                <input
                  v-else-if="attr.type === 'BOOL'"
                  v-model="editValue"
                  type="checkbox"
                  class="form-checkbox"
                  @change="save(attr)"
                />
                <!-- Có thể mở rộng cho SELECT -->
              </template>
              <template v-else>
                <span v-if="attr.type === 'BOOL'">
                  <span v-if="getValue(attr.id) === 'true'">✔️</span>
                  <span v-else>❌</span>
                </span>
                <span v-else>{{ getValue(attr.id) }}</span>
              </template>
            </td>
            <td class="px-3 py-2 text-sm">{{ attr.unit || '' }}</td>
            <td class="px-3 py-2 text-sm text-right">
              <button v-if="editId !== attr.id" @click="startEdit(attr)" class="text-blue-600 hover:underline text-xs">Sửa</button>
              <button v-else @click="cancelEdit" class="text-gray-500 hover:underline text-xs ml-2">Hủy</button>
            </td>
          </tr>
          <tr v-if="attributes.length === 0">
            <td colspan="4" class="text-center text-gray-400 py-6">Không có thông số kỹ thuật</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { api } from '../api'

const props = defineProps({
  productId: { type: [Number, String], required: true },
  categoryId: { type: [Number, String], required: true }
})

const attributes = ref([])
const values = ref([])
const loading = ref(false)
const editId = ref(null)
const editValue = ref('')

const fetchData = async () => {
  if (!props.productId || !props.categoryId) return
  loading.value = true
  try {
    const [attrRes, valRes] = await Promise.all([
      api.get(`/api/product-attributes/category/${props.categoryId}`),
      api.get(`/api/product-attribute-values/product/${props.productId}`)
    ])
    attributes.value = attrRes.data
    values.value = valRes.data
  } catch (e) {
    attributes.value = []
    values.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
watch(() => [props.productId, props.categoryId], fetchData)

function getValue(attributeId) {
  const found = values.value.find(v => v.attributeId === attributeId)
  return found ? found.value : ''
}

function startEdit(attr) {
  editId.value = attr.id
  editValue.value = getValue(attr.id)
  if (attr.type === 'BOOL') {
    editValue.value = getValue(attr.id) === 'true'
  }
}

function cancelEdit() {
  editId.value = null
  editValue.value = ''
}

async function save(attr) {
  const val = attr.type === 'BOOL' ? (editValue.value ? 'true' : 'false') : editValue.value
  let found = values.value.find(v => v.attributeId === attr.id)
  try {
    if (found) {
      // Update
      await api.put(`/api/product-attribute-values/${found.id}`, {
        ...found,
        value: val
      })
      found.value = val
    } else {
      // Create
      const res = await api.post(`/api/product-attribute-values`, {
        productId: props.productId,
        attributeId: attr.id,
        value: val
      })
      values.value.push(res.data)
    }
    cancelEdit()
  } catch (e) {
    alert('Lưu thất bại!')
  }
}
</script> 