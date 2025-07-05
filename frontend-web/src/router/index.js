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
    path: '/orders',
    name: 'Orders',
    component: () => import('../views/Orders.vue'),
    meta: { title: 'Quản lý đơn hàng' }
  },
  {
    path: '/vehicles',
    name: 'Vehicles',
    component: () => import('../views/Vehicles.vue'),
    meta: { title: 'Quản lý mẫu xe' }
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
