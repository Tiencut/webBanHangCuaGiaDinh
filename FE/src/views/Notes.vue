<template>
  <div class="space-y-6">
    <div class="flex items-center justify-between">
      <h1 class="text-2xl font-semibold text-gray-900">Ghi chú</h1>
      <div class="flex items-center space-x-2">
        <input v-model="filters.keyword" type="text" placeholder="Tìm theo tiêu đề/nội dung" class="border rounded-lg px-3 py-2 text-sm" />
        <select v-model="filters.completed" class="border rounded-lg px-3 py-2 text-sm">
          <option :value="null">Tất cả</option>
          <option :value="false">Chưa xong</option>
          <option :value="true">Đã xong</option>
        </select>
        <button @click="fetchNotes" class="px-3 py-2 rounded-lg bg-[#0070F4] text-white text-sm">Lọc</button>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow p-4 space-y-3">
      <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
        <input v-model="form.title" type="text" placeholder="Tiêu đề" class="border rounded-lg px-3 py-2 text-sm" />
        <input v-model="form.content" type="text" placeholder="Nội dung" class="border rounded-lg px-3 py-2 text-sm" />
        <button @click="createNote" class="px-3 py-2 rounded-lg bg-[#0070F4] text-white text-sm">Thêm ghi chú</button>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow">
      <table class="w-full">
        <thead>
          <tr class="bg-gray-50 text-left text-sm text-gray-600">
            <th class="p-3">Xong</th>
            <th class="p-3">Tiêu đề</th>
            <th class="p-3">Nội dung</th>
            <th class="p-3">Hạn</th>
            <th class="p-3">Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="note in notes" :key="note.id" class="border-t text-sm">
            <td class="p-3">
              <input type="checkbox" :checked="note.completed" @change="toggleCompleted(note)" />
            </td>
            <td class="p-3">
              <input v-model="note.title" @blur="saveNote(note)" class="border rounded px-2 py-1 w-full" />
            </td>
            <td class="p-3">
              <input v-model="note.content" @blur="saveNote(note)" class="border rounded px-2 py-1 w-full" />
            </td>
            <td class="p-3">
              <input type="datetime-local" :value="formatDateInput(note.dueDate)" @change="updateDueDate(note, $event)" class="border rounded px-2 py-1" />
            </td>
            <td class="p-3">
              <button @click="removeNote(note.id)" class="px-2 py-1 bg-red-500 text-white rounded">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="flex items-center justify-between p-3 border-t text-sm">
        <div>Tổng: {{ total }}</div>
        <div class="space-x-2">
          <button :disabled="page === 0" @click="page--; fetchNotes()" class="px-2 py-1 border rounded">Trước</button>
          <span>Trang {{ page + 1 }}</span>
          <button :disabled="(page + 1) * size >= total" @click="page++; fetchNotes()" class="px-2 py-1 border rounded">Sau</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { notesApi } from '@/api'
import { useAuthStore } from '@/stores/auth'

export default {
  name: 'Notes',
  setup() {
    const auth = useAuthStore()
    const notes = ref([])
    const total = ref(0)
    const page = ref(0)
    const size = ref(10)
    const filters = ref({ keyword: '', completed: null })

    const form = ref({ title: '', content: '' })

    const fetchNotes = async () => {
      await auth.getCurrentUser()
      if (!auth.user?.id) return
      const res = await notesApi.list(auth.user.id, { page: page.value, size: size.value, keyword: filters.value.keyword, completed: filters.value.completed })
      const data = res.data
      notes.value = data.content || []
      total.value = data.totalElements || 0
    }

    const createNote = async () => {
      if (!form.value.title) return
      const payload = { userId: auth.user.id, title: form.value.title, content: form.value.content }
      await notesApi.create(payload)
      form.value = { title: '', content: '' }
      fetchNotes()
    }

    const saveNote = async (note) => {
      const payload = { title: note.title, content: note.content, completed: note.completed, dueDate: note.dueDate }
      await notesApi.update(note.id, payload)
    }

    const toggleCompleted = async (note) => {
      note.completed = !note.completed
      await saveNote(note)
    }

    const removeNote = async (id) => {
      await notesApi.delete(id)
      fetchNotes()
    }

    const formatDateInput = (dt) => {
      if (!dt) return ''
      const d = new Date(dt)
      const pad = (n) => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
    }

    const updateDueDate = async (note, e) => {
      const val = e.target.value
      note.dueDate = val ? new Date(val).toISOString() : null
      await saveNote(note)
    }

    onMounted(fetchNotes)

    return { notes, total, page, size, filters, form, fetchNotes, createNote, saveNote, toggleCompleted, removeNote, formatDateInput, updateDueDate }
  }
}
</script>

