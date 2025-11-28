<template>
  <button
    :disabled="loading || applied"
    @click="apply"
    :class="[
      'px-3 py-1 rounded-md text-sm font-medium',
      applied ? 'bg-green-50 text-green-600' : 'bg-yellow-50 text-yellow-700 hover:bg-yellow-100'
    ]"
    title="Áp giá"
  >
    <span v-if="loading">Đang áp...</span>
    <span v-else-if="applied">Đã áp giá</span>
    <span v-else>Áp giá</span>
  </button>
</template>

<script>
import { ref } from 'vue'
import { ordersApi } from '@/api'

export default {
  name: 'ApplyPriceButton',
  props: {
    orderId: {
      type: [Number, String],
      required: true
    },
    initialApplied: {
      type: Boolean,
      default: false
    }
  },
  setup(props, { emit }) {
    const loading = ref(false)
    const applied = ref(Boolean(props.initialApplied))

    const apply = async () => {
      if (applied.value || loading.value) return
      loading.value = true
      try {
        const res = await ordersApi.applyPrice(props.orderId)
        applied.value = Boolean(res.data.isPriceApplied)
        // emit event so parent can refresh or update its list
        emit('applied', res.data)
      } catch (err) {
        console.error('Apply price error', err)
        // show a simple alert for now
        alert(err?.response?.data?.message || 'Lỗi khi áp giá')
      } finally {
        loading.value = false
      }
    }

    return { loading, applied, apply }
  }
}
</script>
