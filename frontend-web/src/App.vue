<template>
  <div id="app">
    <nav class="navbar">
      <div class="nav-brand">Hệ thống bán hàng phụ tùng xe tải</div>
      <div class="nav-menu">
        <button @click="currentView = 'products'" :class="{ active: currentView === 'products' }">
          Sản phẩm
        </button>
        <button @click="currentView = 'customers'" :class="{ active: currentView === 'customers' }">
          Khách hàng
        </button>
        <button @click="currentView = 'vehicles'" :class="{ active: currentView === 'vehicles' }">
          Xe
        </button>
        <button @click="currentView = 'orders'" :class="{ active: currentView === 'orders' }">
          Đơn hàng
        </button>
      </div>
    </nav>

    <main class="main-content">
      <Products v-if="currentView === 'products'" />
      <Customers v-if="currentView === 'customers'" />
      <Vehicles v-if="currentView === 'vehicles'" />
      <Orders v-if="currentView === 'orders'" />
    </main>
  </div>
</template>

<script>
import Products from './components/Products.vue'
import Customers from './components/Customers.vue'
import Vehicles from './components/Vehicles.vue'
import Orders from './components/Orders.vue'

export default {
  name: 'App',
  components: {
    Products,
    Customers,
    Vehicles,
    Orders
  },
  data() {
    return {
      currentView: 'products'
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f5f5;
}

#app {
  min-height: 100vh;
}

.navbar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.nav-menu {
  display: flex;
  gap: 1rem;
}

.nav-menu button {
  background: rgba(255,255,255,0.1);
  border: 1px solid rgba(255,255,255,0.2);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.nav-menu button:hover {
  background: rgba(255,255,255,0.2);
}

.nav-menu button.active {
  background: rgba(255,255,255,0.3);
  border-color: rgba(255,255,255,0.5);
}

.main-content {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
</style> 