<template>
  <div v-if="modelValue" class="fixed inset-0 z-50 flex items-center justify-center">
    <div class="fixed inset-0 bg-black/50" @click="onCancel" aria-hidden="true"></div>

    <div class="bg-white rounded-lg shadow-lg max-w-6xl w-full mx-4 z-10 overflow-hidden">
      <header class="flex items-center justify-between px-4 py-3 border-b">
        <h3 class="text-lg font-semibold">Import Preview</h3>
        <button @click="onCancel" class="text-gray-600 hover:text-gray-900" aria-label="Close preview">✕</button>
      </header>

      <section class="p-4">
        <div class="mb-3">
          <p class="text-sm text-gray-700">Showing detected columns and the first {{ maxRows }} data rows from the file.</p>
        </div>

        <div class="mb-4">
          <div v-if="isValid" class="text-sm text-green-700 bg-green-50 inline-block px-3 py-1 rounded">File format OK — required columns found.</div>
          <div v-else class="text-sm text-red-700 bg-red-50 inline-block px-3 py-1 rounded">
            Missing columns: <span class="font-medium">{{ missing.join(', ') || '—' }}</span>
          </div>
          <div v-if="extra.length" class="mt-2 text-xs text-gray-600">Extra columns detected: {{ extra.join(', ') }}</div>
        </div>

        <div class="overflow-auto border rounded">
          <table class="min-w-full text-sm">
            <thead class="bg-gray-50 text-gray-700">
              <tr>
                <th v-for="(h, idx) in parsedHeaders" :key="`h-${idx}`" class="px-3 py-2 text-left border-r">{{ h }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="!previewRows || previewRows.length===0">
                <td :colspan="Math.max(parsedHeaders.length, 1)" class="px-3 py-6 text-center text-gray-500">No rows to preview</td>
              </tr>
              <tr v-for="(row, rIdx) in limitedRows" :key="`r-${rIdx}`" class="even:bg-white odd:bg-gray-50">
                <td v-for="(h, cIdx) in parsedHeaders" :key="`r-${rIdx}-c-${cIdx}`" class="px-3 py-2 align-top border-r">
                  <span class="break-words">{{ cellValue(row, h, cIdx) }}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <footer class="flex items-center justify-end gap-3 px-4 py-3 border-t">
        <button @click="onCancel" class="px-3 py-2 rounded bg-white border text-sm">Cancel</button>
        <button :disabled="!isValid" @click="onConfirm" class="px-3 py-2 rounded text-sm text-white" :class="isValid ? 'bg-blue-600 hover:bg-blue-700' : 'bg-gray-300 cursor-not-allowed'">Confirm Import</button>
      </footer>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: { type: Boolean, required: true },
  expectedHeaders: { type: Array, default: () => [] },
  parsedHeaders: { type: Array, default: () => [] },
  previewRows: { type: Array, default: () => [] },
  maxRows: { type: Number, default: 10 }
});

const emit = defineEmits(['update:modelValue', 'confirm', 'cancel']);

const normalize = (s) => (s === null || s === undefined) ? '' : String(s).trim().toLowerCase();

const expectedLower = computed(() => props.expectedHeaders.map(normalize));
const parsedLower = computed(() => props.parsedHeaders.map(normalize));

const missing = computed(() => {
  return expectedLower.value.filter(h => h && !parsedLower.value.includes(h)).map((h) => {
    const idx = expectedLower.value.indexOf(h);
    return props.expectedHeaders[idx] || h;
  });
});

const extra = computed(() => {
  return parsedLower.value.filter(h => h && !expectedLower.value.includes(h)).map((h) => {
    const idx = parsedLower.value.indexOf(h);
    return props.parsedHeaders[idx] || h;
  });
});

const isValid = computed(() => missing.value.length === 0 && props.parsedHeaders && props.parsedHeaders.length > 0);

const limitedRows = computed(() => (props.previewRows || []).slice(0, props.maxRows));

function cellValue(row, header, colIdx) {
  if (!row) return '';
  if (Array.isArray(row)) return row[colIdx] ?? '';
  if (header in row) return row[header];
  const key = Object.keys(row).find(k => normalize(k) === normalize(header));
  return key ? row[key] : '';
}

function close() {
  emit('update:modelValue', false);
}

function onConfirm() {
  emit('confirm');
  close();
}

function onCancel() {
  emit('cancel');
  close();
}

function onKey(e) {
  if (e.key === 'Escape') onCancel();
}

onMounted(() => window.addEventListener('keydown', onKey));
onUnmounted(() => window.removeEventListener('keydown', onKey));
</script>

<style scoped>
/* minimal containment so modal layout is predictable */
.min-w-full td, .min-w-full th { min-width: 120px; }
table { border-collapse: collapse; }
</style>
