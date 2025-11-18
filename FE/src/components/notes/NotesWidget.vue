<template>
  <div class="bg-white rounded-lg shadow border border-gray-200 max-w-lg mx-auto p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-semibold text-gray-900">Ghi chú</h2>
      <router-link to="/notes" class="text-blue-600 hover:text-blue-800 font-medium text-sm">Xem tất cả</router-link>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 gap-3 mb-6">
      <input
        v-model="form.title"
        type="text"
        placeholder="Tiêu đề"
        class="border rounded-lg px-4 py-2 text-sm focus:ring-2 focus:ring-blue-400 outline-none"
        @keyup.enter="createNote"
      />
    </div>

    <div v-if="loading" class="text-center py-8">
      <svg class="mx-auto h-8 w-8 text-gray-400 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <p class="mt-2 text-sm text-gray-500">Đang tải ghi chú...</p>
    </div>

    <div v-else-if="notes.length === 0" class="text-center py-8 text-gray-500">Chưa có ghi chú nào.</div>

    <div v-else class="space-y-3">
      <div
        v-for="note in notes"
        :key="note.id"
        class="flex items-center justify-between p-3 border border-gray-100 rounded-lg hover:shadow-sm transition-shadow"
      >
        <div class="flex items-center space-x-3">
          <input type="checkbox" :checked="note.completed" @change="toggleCompleted(note)" />
          <div>
            <div class="font-medium text-gray-900">{{ note.title || 'Không có tiêu đề' }}</div>
          </div>
        </div>
        <button @click="removeNote(note.id)" class="px-2 py-1 bg-red-600 hover:bg-red-700 text-white rounded text-sm transition">
          Xóa
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { notesApi } from '@/api'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'NotesWidget',
  setup() {
    const auth = useAuthStore()
    const notes = ref([])
    const loading = ref(false)
    const form = ref({ title: '' })

    const fetchNotes = async () => {
      loading.value = true
      await auth.getCurrentUser()
      if (!auth.user?.id) {
        notes.value = []
        loading.value = false
        return
      }
      const res = await notesApi.list(auth.user.id, { page: 0, size: 5 })
      notes.value = res.data.content || []
      loading.value = false
    }

    const createNote = async () => {
      if (!form.value.title) return
      await auth.getCurrentUser()
      if (!auth.user?.id) return
      const payload = { userId: auth.user.id, title: form.value.title }
      await notesApi.create(payload)
      form.value = { title: '' }
      fetchNotes()
    }

    const toggleCompleted = async (note) => {
      note.completed = !note.completed
      await notesApi.update(note.id, { completed: note.completed })
      fetchNotes()
    }

    const removeNote = async (id) => {
      await notesApi.delete(id)
      fetchNotes()
    }

    onMounted(fetchNotes)

    const canCreate = computed(() => !!auth.user?.id)
    return { notes, loading, form, fetchNotes, createNote, toggleCompleted, removeNote, canCreate }
  },
}
</script>
