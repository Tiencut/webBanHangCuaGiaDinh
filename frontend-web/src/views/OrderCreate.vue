<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">Đặt hàng</h1>
        <p class="text-gray-600 mt-1">Tạo đơn hàng mới cho khách hàng</p>
      </div>
      
      <div class="flex items-center space-x-4">
        <button class="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 transition-colors">
          Hủy
        </button>
        <button class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors">
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
              <input type="text" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Nhập tên khách hàng">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
              <input type="tel" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" placeholder="Nhập số điện thoại">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Địa chỉ</label>
              <textarea class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" rows="3" placeholder="Nhập địa chỉ giao hàng"></textarea>
            </div>
          </div>
        </div>

        <!-- Order Information -->
        <div>
          <h3 class="text-lg font-semibold mb-4">Thông tin đơn hàng</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày đặt hàng</label>
              <input type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ngày giao hàng</label>
              <input type="date" class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Ghi chú</label>
              <textarea class="w-full p-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500" rows="3" placeholder="Ghi chú thêm về đơn hàng"></textarea>
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
              <tr>
                <td class="px-4 py-4 text-sm text-gray-900">Phuộc trước Honda City</td>
                <td class="px-4 py-4 text-sm text-gray-900">1,200,000 VND</td>
                <td class="px-4 py-4">
                  <input type="number" class="w-20 p-1 border border-gray-300 rounded" value="1" min="1">
                </td>
                <td class="px-4 py-4 text-sm text-gray-900">1,200,000 VND</td>
                <td class="px-4 py-4">
                  <button class="text-red-500 hover:text-red-700">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <div class="mt-4 flex justify-between items-center">
          <button class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-colors">
            Thêm sản phẩm
          </button>
          <div class="text-right">
            <p class="text-lg font-semibold">Tổng cộng: 1,200,000 VND</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OrderCreate',
  data() {
    return {
      // Component data
    }
  },
  methods: {
    // Component methods
  }
}
</script>
