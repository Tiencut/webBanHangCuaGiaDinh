import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { title: 'Trang chủ' }
  },
  {
    path: '/products',
    name: 'Products',
    component: () => import('../views/Products.vue'),
    meta: { title: 'Quản lý sản phẩm' }
  },
  {
    path: '/categories',
    name: 'Categories',
    component: () => import('../views/Categories.vue'),
    meta: { title: 'Quản lý danh mục' }
  },
  {
    path: '/pricing',
    name: 'Pricing',
    component: () => import('../views/Pricing.vue'),
    meta: { title: 'Thiết lập giá' }
  },
  {
    path: '/suppliers',
    name: 'Suppliers',
    component: () => import('../views/Suppliers.vue'),
    meta: { title: 'Quản lý nhà cung cấp' }
  },
  {
    path: '/purchase-order',
    name: 'PurchaseOrder',
    component: () => import('../views/PurchaseOrder.vue'),
    meta: { title: 'Nhập hàng' }
  },
  {
    path: '/purchase-returns',
    name: 'PurchaseReturns',
    component: () => import('../views/PurchaseReturns.vue'),
    meta: { title: 'Trả hàng nhập' }
  },
  {
    path: '/table-demo',
    name: 'TableDemo',
    component: () => import('../views/TableDemo.vue'),
    meta: { title: 'Demo Bảng' }
  },
  {
    path: '/table-comparison',
    name: 'TableComparison',
    component: () => import('../views/TableComparison.vue'),
    meta: { title: 'So sánh Bảng' }
  },
  {
    path: '/customers',
    name: 'Customers',
    component: () => import('../views/Customers.vue'),
    meta: { title: 'Quản lý khách hàng' }
  },
  {
    path: '/customers/:id',
    name: 'CustomerDetail',
    component: () => import('../views/CustomerDetail.vue'),
    meta: { title: 'Chi tiết khách hàng' }
  },
  {
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue'),
    meta: { title: 'Quản lý đơn hàng' }
  },
  {
    path: '/order-create',
    name: 'OrderCreate',
    component: () => import('../views/OrderCreate.vue'),
    meta: { title: 'Đặt hàng' }
  },
  {
    path: '/order-returns',
    name: 'OrderReturns',
    component: () => import('../views/OrderReturns.vue'),
    meta: { title: 'Trả hàng' }
  },
  {
    path: '/delivery-partners',
    name: 'DeliveryPartners',
    component: () => import('../views/DeliveryPartners.vue'),
    meta: { title: 'Đối tác giao hàng' }
  },
  {
    path: '/vehicles',
    name: 'Vehicles',
    component: () => import('../views/Vehicles.vue'),
    meta: { title: 'Quản lý mẫu xe' }
  },
  {
    path: '/inventory-check',
    name: 'InventoryCheck',
    component: () => import('../views/InventoryCheck.vue'),
    meta: { title: 'Kiểm kho' }
  },
  {
    path: '/import',
    name: 'ImportData',
    component: () => import('../views/ImportData.vue'),
    meta: { title: 'Import dữ liệu' }
  },
  {
    path: '/sales',
    name: 'Sales',
    component: () => import('../views/Sales.vue'),
    meta: { title: 'Bán hàng' }
  },
  {
    path: '/debts',
    name: 'Debts',
    component: () => import('../views/Debts.vue'),
    meta: { title: 'Quản lý nợ' }
  },
  {
    path: '/product-list-debug',
    name: 'ProductListDebug',
    component: () => import('../views/ProductListDebug.vue'),
    meta: { title: 'Debug Danh sách sản phẩm' }
  }
  ,
  {
    path: '/notes',
    name: 'Notes',
    component: () => import('../views/Notes.vue'),
    meta: { title: 'Ghi chú' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Update page title based on route meta
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - Hệ thống bán hàng phụ tùng xe tải`
  }
  next()
})

export default router
