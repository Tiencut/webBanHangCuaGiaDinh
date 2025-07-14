<template>
  <div class="training-assistant">
    <!-- Toggle Button -->
    <button 
      @click="toggleAssistant"
      class="training-toggle-btn"
      :class="{ 'active': isOpen }"
    >
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014.846 21H9.154a3.374 3.374 0 00-2.819-1.53l-.548-.547z"/>
      </svg>
      <span class="ml-2">Trợ lý học tập</span>
    </button>

    <!-- Assistant Panel -->
    <div v-if="isOpen" class="training-panel">
      <!-- Header -->
      <div class="training-header">
        <h3 class="text-lg font-semibold text-gray-900">🎓 Trợ lý học tập</h3>
        <button @click="isOpen = false" class="text-gray-400 hover:text-gray-600">
          <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
          </svg>
        </button>
      </div>

      <!-- Search Box -->
      <div class="training-search">
        <div class="relative">
          <input
            v-model="searchQuery"
            @input="onSearchInput"
            type="text"
            placeholder="Tìm kiến thức về sản phẩm, xe..."
            class="training-search-input"
          />
          <svg class="training-search-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"/>
          </svg>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="training-actions">
        <button 
          @click="showQuickHelp"
          class="training-action-btn"
        >
          ⚡ Trợ giúp nhanh
        </button>
        <button 
          @click="showProductGuide"
          class="training-action-btn"
        >
          🔍 Nhận diện sản phẩm
        </button>
        <button 
          @click="requestHelp"
          class="training-action-btn help-btn"
        >
          🆘 Cần hỗ trợ
        </button>
      </div>

      <!-- Content Area -->
      <div class="training-content">
        <!-- Loading -->
        <div v-if="loading" class="text-center py-4">
          <div class="animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600 mx-auto"></div>
          <p class="text-sm text-gray-500 mt-2">Đang tìm kiếm...</p>
        </div>

        <!-- Search Results -->
        <div v-else-if="searchResults.length > 0" class="training-results">
          <h4 class="text-sm font-medium text-gray-700 mb-3">Kết quả tìm kiếm:</h4>
          <div 
            v-for="content in searchResults" 
            :key="content.id"
            @click="openContent(content)"
            class="training-result-item"
          >
            <div class="flex items-start space-x-3">
              <div class="training-category-badge" :class="getCategoryClass(content.category)">
                {{ getCategoryIcon(content.category) }}
              </div>
              <div class="flex-1 min-w-0">
                <h5 class="text-sm font-medium text-gray-900 truncate">{{ content.title }}</h5>
                <p class="text-xs text-gray-500 mt-1 line-clamp-2">{{ content.summary }}</p>
                <div class="flex items-center mt-2 space-x-2">
                  <span class="training-priority-badge" :class="getPriorityClass(content.priority)">
                    {{ getPriorityText(content.priority) }}
                  </span>
                  <span v-if="content.estimatedReadTime" class="text-xs text-gray-400">
                    📖 {{ content.estimatedReadTime }} phút
                  </span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Quick Help -->
        <div v-else-if="quickHelpVisible" class="training-quick-help">
          <h4 class="text-sm font-medium text-gray-700 mb-3">💡 Trợ giúp nhanh:</h4>
          <div class="space-y-2">
            <div 
              v-for="tip in quickHelpTips" 
              :key="tip.id"
              class="training-tip-item"
            >
              <div class="flex items-center space-x-2">
                <span class="text-lg">{{ tip.icon }}</span>
                <span class="text-sm font-medium">{{ tip.title }}</span>
              </div>
              <p class="text-xs text-gray-600 mt-1">{{ tip.content }}</p>
            </div>
          </div>
        </div>

        <!-- Product Recognition Guide -->
        <div v-else-if="productGuideVisible" class="training-product-guide">
          <h4 class="text-sm font-medium text-gray-700 mb-3">🔍 Hướng dẫn nhận diện sản phẩm:</h4>
          <div class="space-y-3">
            <div class="training-guide-step">
              <div class="training-step-number">1</div>
              <div>
                <h5 class="text-sm font-medium text-gray-900">Hỏi thông tin xe</h5>
                <p class="text-xs text-gray-600">Thương hiệu, mẫu xe, năm sản xuất</p>
                <div class="training-example">
                  <strong>Ví dụ:</strong> "Anh cho em biết xe Hyundai mẫu gì, năm bao nhiêu ạ?"
                </div>
              </div>
            </div>

            <div class="training-guide-step">
              <div class="training-step-number">2</div>
              <div>
                <h5 class="text-sm font-medium text-gray-900">Xác định phụ tùng</h5>
                <p class="text-xs text-gray-600">Vị trí, chức năng, triệu chứng hỏng</p>
                <div class="training-example">
                  <strong>Gợi ý:</strong> "Má phanh có tiếng kêu không? Trước hay sau?"
                </div>
              </div>
            </div>

            <div class="training-guide-step">
              <div class="training-step-number">3</div>
              <div>
                <h5 class="text-sm font-medium text-gray-900">Kiểm tra tương thích</h5>
                <p class="text-xs text-gray-600">So sánh với database, kiểm tra OEM</p>
                <div class="training-example">
                  <strong>Lưu ý:</strong> HD65 và HD72 dùng má phanh khác nhau!
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-else class="training-empty">
          <div class="text-center py-8">
            <svg class="w-12 h-12 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.746 0 3.332.477 4.5 1.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"/>
            </svg>
            <p class="text-sm text-gray-500 mt-4">Chọn một tính năng bên trên để bắt đầu học</p>
          </div>
        </div>
      </div>

      <!-- Stats Footer -->
      <div class="training-footer">
        <div class="flex justify-between items-center text-xs text-gray-500">
          <span>Đã học: {{ learningStats.understoodContent || 0 }} bài</span>
          <span>Tốc độ: {{ formatSearchTime(learningStats.averageSearchTime) }}</span>
        </div>
      </div>
    </div>

    <!-- Content Modal -->
    <div v-if="selectedContent" class="training-modal" @click="closeModal">
      <div class="training-modal-content" @click.stop>
        <div class="training-modal-header">
          <h3 class="text-lg font-semibold text-gray-900">{{ selectedContent.title }}</h3>
          <button @click="closeModal" class="text-gray-400 hover:text-gray-600">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
            </svg>
          </button>
        </div>
        <div class="training-modal-body">
          <div v-html="selectedContent.content" class="training-content-html"></div>
          <div v-if="selectedContent.images" class="training-images mt-4">
            <img 
              v-for="(image, index) in getImageList(selectedContent.images)" 
              :key="index"
              :src="image" 
              :alt="`Hình ${index + 1}`"
              class="training-image"
            />
          </div>
        </div>
        <div class="training-modal-footer">
          <button @click="markAsUnderstood" class="training-understood-btn">
            ✓ Đã hiểu
          </button>
          <button @click="markAsHelpful" class="training-helpful-btn">
            👍 Hữu ích
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'TrainingAssistant',
  props: {
    currentSearchQuery: {
      type: String,
      default: ''
    },
    selectedProductId: {
      type: Number,
      default: null
    }
  },
  emits: ['search-suggestion', 'help-request'],
  setup(props, { emit }) {
    const isOpen = ref(false)
    const loading = ref(false)
    const searchQuery = ref('')
    const searchResults = ref([])
    const selectedContent = ref(null)
    const quickHelpVisible = ref(false)
    const productGuideVisible = ref(false)
    
    const learningStats = reactive({
      understoodContent: 0,
      averageSearchTime: 0,
      helpRequests: 0
    })

    // Mock quick help tips
    const quickHelpTips = ref([
      {
        id: 1,
        icon: '🚛',
        title: 'Hyundai HD Series',
        content: 'HD65 (2015-2020): 3.5 tấn | HD72 (2021+): 5 tấn'
      },
      {
        id: 2,
        icon: '🔧',
        title: 'Má phanh phổ biến',
        content: 'Thay 6 tháng hoặc 30,000km. Luôn thay cả bộ'
      },
      {
        id: 3,
        icon: '⚠️',
        title: 'Lưu ý quan trọng',
        content: 'Kiểm tra OEM number trước khi báo giá'
      }
    ])

    const toggleAssistant = () => {
      isOpen.value = !isOpen.value
      if (isOpen.value) {
        loadLearningStats()
      }
    }

    const onSearchInput = async () => {
      if (searchQuery.value.length < 2) {
        searchResults.value = []
        return
      }

      loading.value = true
      quickHelpVisible.value = false
      productGuideVisible.value = false

      try {
        // Simulate API call
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // Mock search results
        searchResults.value = [
          {
            id: 1,
            title: 'Cách nhận diện má phanh Hyundai HD65',
            summary: 'Hướng dẫn phân biệt má phanh chính hãng và tương thích cho xe Hyundai HD65',
            category: 'PRODUCT_IDENTIFICATION',
            priority: 1,
            estimatedReadTime: 3,
            content: `
              <h4>Má phanh Hyundai HD65 - Điểm nhận biết</h4>
              <ul>
                <li><strong>OEM Number:</strong> 58101-5H000 (trước), 58302-5H000 (sau)</li>
                <li><strong>Kích thước:</strong> Dày 12mm, rộng 140mm</li>
                <li><strong>Logo:</strong> Có dập logo Hyundai hoặc Sangsin</li>
                <li><strong>Màu sắc:</strong> Đen hoặc xám đậm</li>
              </ul>
              <h5>⚠️ Lưu ý quan trọng:</h5>
              <p>- Không dùng cho HD72 (khác kích thước)</p>
              <p>- Luôn thay cả bộ 4 miếng</p>
              <p>- Bảo hành 6 tháng hoặc 30,000km</p>
            `,
            images: '["https://example.com/brake-pad-1.jpg", "https://example.com/brake-pad-2.jpg"]'
          }
        ]
      } catch (error) {
        console.error('Search error:', error)
      } finally {
        loading.value = false
      }
    }

    const showQuickHelp = () => {
      quickHelpVisible.value = true
      productGuideVisible.value = false
      searchResults.value = []
    }

    const showProductGuide = () => {
      productGuideVisible.value = true
      quickHelpVisible.value = false
      searchResults.value = []
    }

    const requestHelp = () => {
      emit('help-request', {
        query: searchQuery.value,
        context: 'training-assistant'
      })
    }

    const openContent = (content) => {
      selectedContent.value = content
      // Mark as viewed
      markAsViewed(content.id)
    }

    const closeModal = () => {
      selectedContent.value = null
    }

    const markAsViewed = async (contentId) => {
      try {
        // API call to mark as viewed
        console.log('Marked as viewed:', contentId)
      } catch (error) {
        console.error('Error marking as viewed:', error)
      }
    }

    const markAsUnderstood = async () => {
      if (!selectedContent.value) return
      
      try {
        // API call to mark as understood
        console.log('Marked as understood:', selectedContent.value.id)
        learningStats.understoodContent++
        closeModal()
      } catch (error) {
        console.error('Error marking as understood:', error)
      }
    }

    const markAsHelpful = async () => {
      if (!selectedContent.value) return
      
      try {
        // API call to mark as helpful
        console.log('Marked as helpful:', selectedContent.value.id)
      } catch (error) {
        console.error('Error marking as helpful:', error)
      }
    }

    const loadLearningStats = async () => {
      try {
        // Mock API call
        learningStats.understoodContent = 15
        learningStats.averageSearchTime = 25.5
        learningStats.helpRequests = 3
      } catch (error) {
        console.error('Error loading stats:', error)
      }
    }

    // Utility methods
    const getCategoryClass = (category) => {
      const classes = {
        'PRODUCT_IDENTIFICATION': 'bg-blue-100 text-blue-800',
        'VEHICLE_KNOWLEDGE': 'bg-green-100 text-green-800',
        'SALES_TIPS': 'bg-purple-100 text-purple-800',
        'TECHNICAL_SPECS': 'bg-orange-100 text-orange-800'
      }
      return classes[category] || 'bg-gray-100 text-gray-800'
    }

    const getCategoryIcon = (category) => {
      const icons = {
        'PRODUCT_IDENTIFICATION': '🔍',
        'VEHICLE_KNOWLEDGE': '🚛',
        'SALES_TIPS': '💡',
        'TECHNICAL_SPECS': '⚙️'
      }
      return icons[category] || '📚'
    }

    const getPriorityClass = (priority) => {
      const classes = {
        1: 'bg-red-100 text-red-800',
        2: 'bg-yellow-100 text-yellow-800',
        3: 'bg-gray-100 text-gray-800'
      }
      return classes[priority] || 'bg-gray-100 text-gray-800'
    }

    const getPriorityText = (priority) => {
      const texts = {
        1: 'Quan trọng',
        2: 'Cần biết',
        3: 'Tham khảo'
      }
      return texts[priority] || 'Khác'
    }

    const getImageList = (images) => {
      try {
        return JSON.parse(images)
      } catch {
        return []
      }
    }

    const formatSearchTime = (time) => {
      if (!time) return 'N/A'
      return `${Math.round(time)}s`
    }

    onMounted(() => {
      // Watch for external search queries
      if (props.currentSearchQuery) {
        searchQuery.value = props.currentSearchQuery
        onSearchInput()
      }
    })

    return {
      isOpen,
      loading,
      searchQuery,
      searchResults,
      selectedContent,
      quickHelpVisible,
      productGuideVisible,
      quickHelpTips,
      learningStats,
      toggleAssistant,
      onSearchInput,
      showQuickHelp,
      showProductGuide,
      requestHelp,
      openContent,
      closeModal,
      markAsUnderstood,
      markAsHelpful,
      getCategoryClass,
      getCategoryIcon,
      getPriorityClass,
      getPriorityText,
      getImageList,
      formatSearchTime
    }
  }
}
</script>

<style scoped>
.training-assistant {
  position: relative;
}

.training-toggle-btn {
  display: flex;
  align-items: center;
  padding: 0.5rem 0.75rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
  background-color: white;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
  transition: all 0.2s;
}

.training-toggle-btn:hover {
  background-color: #f9fafb;
}

.training-toggle-btn.active {
  background-color: #eff6ff;
  border-color: #93c5fd;
  color: #1d4ed8;
}

.training-panel {
  position: absolute;
  top: 3rem;
  right: 0;
  width: 20rem;
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
  border: 1px solid #e5e7eb;
  z-index: 50;
  max-height: 600px;
  overflow-y: auto;
}

.training-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.training-search {
  padding: 0.75rem;
}

.training-search .relative {
  position: relative;
}

.training-search-input {
  width: 100%;
  padding: 0.5rem 1rem 0.5rem 2.5rem;
  font-size: 0.875rem;
  border: 1px solid #d1d5db;
  border-radius: 0.5rem;
}

.training-search-input:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.training-search-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  height: 1rem;
  width: 1rem;
  color: #9ca3af;
}

.training-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.75rem;
  border-bottom: 1px solid #f3f4f6;
}

.training-action-btn {
  padding: 0.25rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 500;
  background-color: #f3f4f6;
  color: #374151;
  border-radius: 9999px;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.training-action-btn:hover {
  background-color: #e5e7eb;
}

.training-action-btn.help-btn {
  background-color: #fed7aa;
  color: #c2410c;
}

.training-action-btn.help-btn:hover {
  background-color: #fdba74;
}

.training-content {
  padding: 0.75rem;
  min-height: 200px;
}

.training-result-item {
  padding: 0.75rem;
  cursor: pointer;
  border-radius: 0.5rem;
  border: 1px solid #f3f4f6;
  margin-bottom: 0.5rem;
  transition: all 0.2s;
}

.training-result-item:hover {
  background-color: #f9fafb;
}

.training-category-badge {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
  font-weight: 500;
  border-radius: 9999px;
}

.training-priority-badge {
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
  font-weight: 500;
  border-radius: 9999px;
}

.training-tip-item {
  padding: 0.75rem;
  background-color: #eff6ff;
  border-radius: 0.5rem;
  margin-bottom: 0.5rem;
}

.training-guide-step {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.training-step-number {
  flex-shrink: 0;
  width: 1.5rem;
  height: 1.5rem;
  background-color: #2563eb;
  color: white;
  font-size: 0.75rem;
  font-weight: bold;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.training-example {
  margin-top: 0.5rem;
  padding: 0.5rem;
  background-color: #f9fafb;
  border-radius: 0.25rem;
  font-size: 0.75rem;
}

.training-footer {
  padding: 0.75rem;
  border-top: 1px solid #f3f4f6;
  background-color: #f9fafb;
}

.training-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 50;
}

.training-modal-content {
  background-color: white;
  border-radius: 0.5rem;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  max-width: 42rem;
  width: 100%;
  margin: 1rem;
  max-height: 80vh;
  overflow-y: auto;
}

.training-modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.training-modal-body {
  padding: 1rem;
}

.training-content-html h4 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #111827;
  margin-bottom: 0.75rem;
}

.training-content-html h5 {
  font-size: 1rem;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 0.5rem;
  margin-top: 1rem;
}

.training-content-html ul {
  list-style-type: disc;
  list-style-position: inside;
  margin-bottom: 1rem;
}

.training-content-html li {
  margin-bottom: 0.25rem;
}

.training-images {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.5rem;
  margin-top: 1rem;
}

.training-image {
  border-radius: 0.5rem;
  border: 1px solid #e5e7eb;
  max-width: 100%;
}

.training-modal-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.75rem;
  padding: 1rem;
  border-top: 1px solid #e5e7eb;
}

.training-understood-btn {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: white;
  background-color: #059669;
  border-radius: 0.5rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.training-understood-btn:hover {
  background-color: #047857;
}

.training-helpful-btn {
  padding: 0.5rem 1rem;
  font-size: 0.875rem;
  font-weight: 500;
  color: #2563eb;
  background-color: #dbeafe;
  border-radius: 0.5rem;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.training-helpful-btn:hover {
  background-color: #bfdbfe;
}

.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* Category colors */
.bg-blue-100 { background-color: #dbeafe; }
.text-blue-800 { color: #1e40af; }
.bg-green-100 { background-color: #dcfce7; }
.text-green-800 { color: #166534; }
.bg-purple-100 { background-color: #e9d5ff; }
.text-purple-800 { color: #6b21a8; }
.bg-orange-100 { background-color: #fed7aa; }
.text-orange-800 { color: #9a3412; }
.bg-gray-100 { background-color: #f3f4f6; }
.text-gray-800 { color: #1f2937; }

/* Priority colors */
.bg-red-100 { background-color: #fee2e2; }
.text-red-800 { color: #991b1b; }
.bg-yellow-100 { background-color: #fef3c7; }
.text-yellow-800 { color: #92400e; }
</style>
