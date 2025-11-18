<template>
  <div class="w-full space-y-6">
    <!-- Quick Stats Cards (KiotViet Style) -->
    <QuickStats />

    <!-- Quick Actions Buttons -->
    <QuickActions />

    <!-- Recent Activity Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <!-- Recent Orders -->
    <RecentOrders :formatCurrency="formatCurrency" />

      <LowStockAlert />

      <NotesWidget />

      <RecentCustomers />
    </div>
  </div>
</template>

<script>
import RecentCustomers from '@/components/home/RecentCustomers.vue'
import { ref, onMounted } from 'vue'
import NotesWidget from '@/components/notes/NotesWidget.vue'
import QuickActions from '@/components/home/QuickActions.vue'
import QuickStats from '@/components/home/QuickStats.vue'
import RecentOrders from '@/components/home/RecentOrders.vue'
import LowStockAlert from '@/components/home/LowStockAlert.vue'
export default {
  name: 'Home',
  components: { NotesWidget, QuickActions, QuickStats, RecentOrders, RecentCustomers, LowStockAlert },
  setup() {
    return {
    }
  }
}
</script>
