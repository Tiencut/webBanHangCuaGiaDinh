<template>
  <div class="w-full space-y-6">
    <!-- Quick Stats + Quick Actions (same row on md+) -->
    <div class="flex flex-col md:flex-row md:items-start md:justify-between md:gap-6">
      <div class="w-full">
        <QuickStats />
      </div>
    </div>

    <!-- Recent Activity Cards -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <!-- Recent Orders -->
      <RecentOrders />

      <!-- Price applied list -->
      <PriceAppliedList />

    <!--  -->
    <LowStockAlert />

    <!--  -->
    <NotesWidget />

    <RecentCustomers />
    </div>
  </div>
</template>

<script>
import RecentCustomers from '@/components/home/RecentCustomers.vue'
import { ref, onMounted } from 'vue'
import NotesWidget from '@/components/notes/NotesWidget.vue'
import QuickStats from '@/components/home/QuickStats.vue'
import RecentOrders from '@/components/home/RecentOrders.vue'
import LowStockAlert from '@/components/home/LowStockAlert.vue'
import PriceAppliedList from '@/components/purchase/PriceAppliedList.vue'
export default {
  name: 'Home',
  components: { NotesWidget, QuickStats, RecentOrders, RecentCustomers, LowStockAlert, PriceAppliedList },
  setup() {
    return {
    }
  }
}
</script>
