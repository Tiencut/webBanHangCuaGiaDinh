<template>
  <div id="app" class="min-h-screen bg-gray-50">
    <!-- Header with Navigation -->
    <AppHeader />
    
    <!-- Main Content Area -->
    <div class="pt-16">
      <div class="w-full px-2 sm:px-4 py-4">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import AppHeader from './components/layout/AppHeader.vue'

export default {
  name: 'App',
  components: {
    AppHeader
  }
}
</script>

<style>
/* Global styles */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f9fafb;
}

#app {
  width: 100%;
  min-height: 100vh;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* Dropdown Menu Styles */
.group:hover .group-hover\:opacity-100 {
  opacity: 1;
}

.group:hover .group-hover\:visible {
  visibility: visible;
}

/* Smooth transitions */
.transition-all {
  transition: all 0.3s ease;
}

/* Focus styles */
.focus-ring {
  outline: none;
  box-shadow: 0 0 0 2px #0070F4;
}

/* Button styles */
.btn-primary {
  background-color: #0070F4;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #374151;
  padding: 8px 16px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.btn-secondary:hover {
  background-color: #e5e7eb;
}

/* Card styles */
.card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  padding: 24px;
}

/* Table styles */
.table {
  min-width: 100%;
  border-collapse: collapse;
}

.table th {
  padding: 12px 24px;
  text-align: left;
  font-size: 12px;
  font-weight: 500;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.table td {
  padding: 16px 24px;
  font-size: 14px;
  color: #111827;
}

/* Form styles */
.form-input {
  display: block;
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-input:focus {
  border-color: #0070F4;
  box-shadow: 0 0 0 2px rgba(0, 112, 244, 0.1);
}

.form-label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  margin-bottom: 4px;
}

/* Animation classes */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter-from {
  transform: translateX(-100%);
}

.slide-leave-to {
  transform: translateX(-100%);
}

/* Responsive utilities */
@media (max-width: 640px) {
  .mobile-hidden {
    display: none !important;
  }
}

@media (min-width: 1024px) {
  .desktop-hidden {
    display: none !important;
  }
}
</style>
