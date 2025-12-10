<template>
  <div class="supplier-picker">
    <div class="picker-header">
      <h4>Chọn nhà cung cấp</h4>
      <button @click="$emit('close')" class="btn-close">×</button>
    </div>
    <div v-if="loading">Đang tải...</div>
    <div v-else>
      <ul class="supplier-list">
        <li v-for="inv in inventories" :key="inv.id" class="supplier-row">
          <div class="supplier-info">
            <div class="name">{{ inv.supplier.name }}</div>
            <div class="meta">Có sẵn: {{ inv.availableQuantity || inv.currentQuantity || 0 }}</div>
          </div>
          <div class="actions">
            <button @click="select(inv)" class="btn-select">Chọn</button>
          </div>
        </li>
      </ul>
      <div v-if="inventories.length === 0">Không có nhà cung cấp có hàng</div>
    </div>
  </div>
</template>

<script>
import { inventoryAPI } from '@/services'

export default {
  name: 'SupplierPicker',
  props: {
    productId: { type: Number, required: true }
  },
  data() {
    return {
      inventories: [],
      loading: false
    }
  },
  async mounted() {
    this.loading = true
    try {
      const res = await inventoryAPI.getByProductId(this.productId)
      this.inventories = res.data || []
    } catch (e) {
      console.error('Failed to load inventories', e)
      this.inventories = []
    } finally {
      this.loading = false
    }
  },
  methods: {
    select(inv) {
      this.$emit('select', inv)
    }
  }
}
</script>

<style scoped>
.supplier-list { list-style: none; padding: 0; margin: 0 }
.supplier-row { display:flex; justify-content:space-between; padding:8px 0; border-bottom:1px solid #eee }
.supplier-info .name { font-weight:600 }
.btn-select { background:#2b6cb0; color:#fff; padding:6px 8px; border-radius:4px }
.picker-header { display:flex; justify-content:space-between; align-items:center }
</style>
