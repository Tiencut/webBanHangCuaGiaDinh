<template>
  <div class="p-6 max-w-3xl mx-auto">
    <h2 class="text-2xl font-semibold mb-4">Bản nháp đơn hàng</h2>

    <div v-if="loading">Đang tải...</div>
    <div v-else>
      <div v-if="drafts.length === 0" class="text-gray-600">Chưa có bản nháp nào.</div>

      <ul class="space-y-3 mt-4">
        <li v-for="d in drafts" :key="d.id" class="p-3 border rounded flex justify-between items-center">
          <div>
            <div class="font-medium">{{ d.name }}</div>
            <div class="text-sm text-gray-500">Tạo: {{ formatDate(d.createdAt || d.created_at) }}</div>
          </div>
          <div class="space-x-2">
            <button @click="load(d.id)" class="px-3 py-1 bg-blue-500 text-white rounded">Tải</button>
            <button @click="edit(d.id)" class="px-3 py-1 bg-yellow-400 text-white rounded">Chỉnh sửa</button>
            <button @click="remove(d.id)" class="px-3 py-1 bg-red-500 text-white rounded">Xóa</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { draftsAPI } from '../services'

const drafts = ref([])
const loading = ref(false)
const router = useRouter()

function formatDate(d) {
  if (!d) return '-'
  const dt = new Date(d)
  return dt.toLocaleString()
}

async function loadList() {
  loading.value = true
  try {
    const res = await draftsAPI.getAll()
    drafts.value = res && res.data ? res.data : res
  } catch (e) {
    console.error(e)
    drafts.value = []
  } finally {
    loading.value = false
  }
}

function load(id) {
  // load into editor without navigation (navigate to order-create with query)
  router.push({ path: '/order-create', query: { draftId: id } })
}

function edit(id) {
  router.push({ path: '/order-create', query: { draftId: id } })
}

async function remove(id) {
  if (!confirm('Xác nhận xóa bản nháp?')) return
  try {
    await draftsAPI.delete(id)
    await loadList()
  } catch (e) {
    console.error(e)
    alert('Xóa thất bại')
  }
}

onMounted(() => loadList())
</script>

<style scoped>
.p-6 { padding: 1.5rem }
</style>
