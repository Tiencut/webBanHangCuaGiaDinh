<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Đặt hàng</h1>
        <p class="text-gray-600 mt-1">Tạo đơn hàng mới cho khách hàng</p>
      </div>
      
      <div class="flex items-center space-x-4">
        <button @click="resetForm" class="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition-colors">
          Hủy
        </button>
        <button @click="submitOrder" class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors">
          Lưu đơn hàng
        </button>
      </div>
    </div>

    <!-- Order Form -->
    <div class="bg-white rounded-lg shadow-md p-6">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Customer Information -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Thông tin khách hàng</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Tên khách hàng</label>
              <input v-model="form.customer.name" type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Nhập tên khách hàng">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
              <input v-model="form.customer.phone" type="tel" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Nhập số điện thoại">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
              <textarea v-model="form.customer.address" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" rows="3" placeholder="Nhập địa chỉ giao hàng"></textarea>
            </div>
          </div>
        </div>

        <!-- Order Information -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Thông tin đơn hàng</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày đặt hàng</label>
              <input v-model="form.orderDate" type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày giao hàng</label>
              <input v-model="form.deliveryDate" type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ghi chú</label>
              <textarea v-model="form.note" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" rows="3" placeholder="Ghi chú thêm về đơn hàng"></textarea>
            </div>
          </div>
        </div>
      </div>

      <!-- Product Selection -->
      <div class="mt-8">
        <h3 class="text-lg font-semibold mb-4">Chọn sản phẩm</h3>
        <div class="border border-gray-200 rounded-lg overflow-hidden">
          <table class="w-full">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-sm font-medium text-gray-700">Sản phẩm</th>
                <th class="px-4 py-3 text-left text-sm font-medium text-gray-700">Giá</th>
                <th class="px-4 py-3 text-left text-sm font-medium text-gray-700">Số lượng</th>
                <th class="px-4 py-3 text-left text-sm font-medium text-gray-700">Thành tiền</th>
                <th class="px-4 py-3 text-left text-sm font-medium text-gray-700">Thao tác</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <tr v-for="(item, idx) in form.items" :key="idx">
                <td class="px-4 py-4 text-sm text-gray-900">
                  <input v-model="item.productName" type="text" class="w-full p-1 border border-gray-200 rounded" placeholder="Tên sản phẩm">
                </td>
                <td class="px-4 py-4 text-sm text-gray-900">
                  <input v-model.number="item.price" type="number" class="w-36 p-1 border border-gray-200 rounded" min="0">
                </td>
                <td class="px-4 py-4">
                  <input v-model.number="item.quantity" type="number" class="w-20 p-1 border border-gray-200 rounded" value="1" min="1">
                </td>
                <td class="px-4 py-4 text-sm text-gray-900">{{ formatCurrency(item.price * item.quantity) }}</td>
                <td class="px-4 py-4">
                  <button @click.prevent="removeItem(idx)" class="text-red-500 hover:text-red-700">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div class="mt-2">
          <button @click.prevent="addItem" class="px-3 py-1 bg-gray-200 rounded">Thêm dòng</button>
        </div>

        <div class="mt-4 flex justify-between items-center">
          <div class="flex items-center space-x-3">
            <button class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-colors" @click="saveDraft">
              Lưu tạm
            </button>
            <button class="bg-indigo-600 text-white px-4 py-2 rounded-lg hover:bg-indigo-700 transition-colors" @click.prevent="payWithProvider('VNPAY')">
              Thanh toán VNPAY
            </button>
            <div class="text-sm text-gray-600">
              <span v-if="autosaveStatus==='saving'">Đang lưu...</span>
              <span v-if="autosaveStatus==='saved'">Đã lưu lúc {{ lastSavedAt ? new Date(lastSavedAt).toLocaleTimeString() : '' }}</span>
              <span v-if="autosaveStatus==='error'" class="text-red-500">Lỗi: {{ autosaveError }}</span>
            </div>
          </div>
          <div class="text-right">
            <p class="text-lg font-semibold">Tổng cộng: {{ formatCurrency(total) }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { draftsAPI, checkoutAPI, ordersAPI } from '../services'
import useDraftAutosave from '../composables/useDraftAutosave'

export default {
  name: 'OrderCreate',
  data() {
    return {
      form: {
        customer: { name: '', phone: '', address: '' },
        orderDate: new Date().toISOString().slice(0,10),
        deliveryDate: '',
        note: '',
        items: [ { productName: '', price: 0, quantity: 1 } ]
      },
      loading: false,
      editingDraftId: null,
      autosaveStatus: '',
      lastSavedAt: null,
      autosaveError: null
    }
  },
  computed: {
    total() {
      return this.form.items.reduce((s, it) => s + (Number(it.price || 0) * Number(it.quantity || 0)), 0)
    }
  },
  methods: {
    formatCurrency(v) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(v)
    },
    addItem() {
      this.form.items.push({ productName: '', price: 0, quantity: 1 })
    },
    removeItem(idx) {
      this.form.items.splice(idx, 1)
    },
    resetForm() {
      this.form = {
        customer: { name: '', phone: '', address: '' },
        orderDate: new Date().toISOString().slice(0,10),
        deliveryDate: '',
        note: '',
        items: [ { productName: '', price: 0, quantity: 1 } ]
      }
      this.editingDraftId = null
    },
    async submitOrder() {
      // placeholder: implement actual save/submit order flow
      alert('Chức năng lưu đơn hàng chưa được triển khai trong task này.')
    },
    async saveDraft() {
      try {
        this.loading = true
        const payloadObj = {
          customer: this.form.customer,
          orderDate: this.form.orderDate,
          deliveryDate: this.form.deliveryDate,
          note: this.form.note,
          items: this.form.items
        }

        const draftPayload = {
          name: this.form.customer.name ? `Draft - ${this.form.customer.name} - ${new Date().toISOString()}` : `Draft - ${new Date().toISOString()}`,
          payload: JSON.stringify(payloadObj)
        }

        if (this.editingDraftId) {
          await draftsAPI.update(this.editingDraftId, draftPayload)
          alert('Đã cập nhật bản nháp')
        } else {
          await draftsAPI.save(draftPayload)
          alert('Đã lưu tạm')
        }
      } catch (e) {
        console.error(e)
        alert('Lưu tạm thất bại')
      } finally {
        this.loading = false
      }
    },
    async payWithProvider(provider = 'VNPAY') {
      try {
        if (!this.form || !this.form.items || this.form.items.length === 0) {
          alert('Không có sản phẩm để thanh toán')
          return
        }
        // create order first (server-side) then initiate checkout
        const createRes = await ordersAPI.create({
          customerId: null, // fill as needed
          items: this.form.items,
          note: this.form.note
        })
        const order = createRes.data || createRes
        const res = await checkoutAPI.initiate(order.id, provider)
        const data = res.data || res
        const redirectUrl = data.redirectUrl
        // open payment page
        const win = window.open(redirectUrl, '_blank')
        // poll order status every 3s
        const poll = setInterval(async () => {
          try {
            const statusRes = await ordersAPI.getById(order.id)
            const orderData = statusRes.data || statusRes
            if (orderData.paymentStatus === 'PAID' || orderData.paymentStatus === 'SUCCESS') {
              clearInterval(poll)
              if (win) win.close()
              alert('Thanh toán thành công')
              // optionally reload order
            }
          } catch (e) {
            console.error('poll error', e)
          }
        }, 3000)
      } catch (e) {
        console.error(e)
        alert('Lỗi khởi tạo thanh toán')
      }
    },
    // silent save used by autosave (no user alerts)
    async saveDraftSilent() {
      try {
        this.loading = true
        const payloadObj = {
          customer: this.form.customer,
          orderDate: this.form.orderDate,
          deliveryDate: this.form.deliveryDate,
          note: this.form.note,
          items: this.form.items
        }

        const draftPayload = {
          name: this.form.customer.name ? `Draft - ${this.form.customer.name} - ${new Date().toISOString()}` : `Draft - ${new Date().toISOString()}`,
          payload: JSON.stringify(payloadObj)
        }

        if (this.editingDraftId) {
          await draftsAPI.update(this.editingDraftId, draftPayload)
        } else {
          await draftsAPI.save(draftPayload)
        }
      } catch (e) {
        console.error('silent save failed', e)
        this.autosaveError = e.message || 'Lưu tạm thất bại'
      } finally {
        this.loading = false
      }
    },
    setAutosaveSaving() {
      this.autosaveStatus = 'saving'
      this.autosaveError = null
    },
    setAutosaveSaved() {
      this.autosaveStatus = 'saved'
      this.lastSavedAt = new Date()
      this.autosaveError = null
    },
    setAutosaveError(msg) {
      this.autosaveStatus = 'error'
      this.autosaveError = msg
    },
    async loadDraftById(id) {
      try {
        this.loading = true
        const res = await draftsAPI.getById(id)
        const data = res && res.data ? res.data : res
        const payload = typeof data.payload === 'string' ? JSON.parse(data.payload) : data.payload
        this.form.customer = payload.customer || { name: '', phone: '', address: '' }
        this.form.orderDate = payload.orderDate || new Date().toISOString().slice(0,10)
        this.form.deliveryDate = payload.deliveryDate || ''
        this.form.note = payload.note || ''
        this.form.items = payload.items && payload.items.length ? payload.items : [ { productName: '', price: 0, quantity: 1 } ]
        this.editingDraftId = id
      } catch (e) {
        console.error(e)
        alert('Không thể tải bản nháp')
      } finally {
        this.loading = false
      }
    }
  },
  async mounted() {
    // if route contains draftId, load it
    const params = new URLSearchParams(window.location.search)
    const draftId = params.get('draftId')
    if (draftId) {
      await this.loadDraftById(draftId)
      // remove query param (optional)
      window.history.replaceState({}, document.title, window.location.pathname)
    }
    // start autosave (save every 30s)
    try {
      this._autosave = useDraftAutosave({ saveFn: async () => {
          this.setAutosaveSaving()
          try {
            await this.saveDraftSilent()
            this.setAutosaveSaved()
          } catch (e) {
            this.setAutosaveError(e.message || 'Lỗi lưu tự động')
          }
        }, getPayload: () => ({ ...this.form }), debounceMs: 3000, storageKey: 'order-autosave' })

      // restore local draft if exists
      const local = this._autosave.loadLocal()
      if (local && local.payload) {
        // ask user to restore
        if (confirm('Tìm thấy bản nháp cục bộ. Khôi phục?')) {
          const p = local.payload
          this.form = p
        }
      }

      // start listening to changes: deep watch
      this.$watch(() => this.form, () => {
        if (this._autosave) this._autosave.notifyChange()
      }, { deep: true })

    } catch (e) {
      console.error('failed to start autosave', e)
    }
  },
  beforeUnmount() {
    if (this._autosave && this._autosave.stop) this._autosave.stop()
  }
}
</script>
