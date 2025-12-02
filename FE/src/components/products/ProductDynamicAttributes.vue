<template>
  <div class="p-2">
    <div v-if="loading" class="text-gray-500">Đang tải thông số...</div>
    <div v-else>
      <div v-if="attributes.length === 0" class="text-sm text-gray-400">Không có thông số kỹ thuật</div>
      <ul v-else class="space-y-2 text-sm">
        <li v-for="attr in attributes" :key="attr.id">
          <span class="font-medium">{{ attr.name }}:</span> {{ attr.value }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import api from '@/services/api'

const props = defineProps({
  'product-id': [String, Number],
  'producta_id': [String, Number],
  'category-id': [String, Number],
  'categorya_id': [String, Number]
})

const loading = ref(false)
const attributes = ref([])

const productId = computed(() => props['product-id'] || props.producta_id)
const categoryId = computed(() => props['category-id'] || props.categorya_id)

const fetchAttributes = async () => {
  if (!productId.value && !categoryId.value) return
  loading.value = true
  try {
    // Try to call API if available. Use fallback empty list.
    // const res = await api.get(`/products/${productId.value}/attributes`)
    // attributes.value = res.data || []
    attributes.value = []
  } catch (e) {
    attributes.value = []
  } finally {
    loading.value = false
  }
}

onMounted(fetchAttributes)
watch([() => productId.value, () => categoryId.value], fetchAttributes)
</script>

<style scoped>
/* minimal styles */
</style>
