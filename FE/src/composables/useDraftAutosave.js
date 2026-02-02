import { ref, watch } from 'vue'
import { draftsAPI } from '../services'

// Simple autosave composable for order drafts
// Usage: const autosave = useDraftAutosave({ saveFn, intervalMs, storageKey, getPayload })
export default function useDraftAutosave({ saveFn, getPayload, debounceMs = 3000, storageKey = 'order-autosave' } = {}) {
    if (typeof saveFn !== 'function') throw new Error('saveFn is required')

    let debounceTimer = null
    let pending = false

    const persistLocal = async(payload) => {
        try {
            if (!payload) return
            localStorage.setItem(storageKey, JSON.stringify({ payload, updatedAt: new Date().toISOString() }))
        } catch (e) {
            console.error('autosave local persist error', e)
        }
    }

    const doSave = async() => {
        try {
            pending = true
            await saveFn()
            // persist what caller provides
            const payload = getPayload ? getPayload() : null
            await persistLocal(payload)
            pending = false
        } catch (e) {
            pending = false
            // still persist locally
            try { await persistLocal(getPayload ? getPayload() : null) } catch (_) {}
            throw e
        }
    }

    const notifyChange = () => {
        if (debounceTimer) clearTimeout(debounceTimer)
        debounceTimer = setTimeout(() => {
            doSave().catch((e) => console.error('autosave error', e))
        }, debounceMs)
    }

    const forceSave = async() => {
        if (debounceTimer) {
            clearTimeout(debounceTimer)
            debounceTimer = null
        }
        await doSave()
    }

    const stop = () => {
        if (debounceTimer) {
            clearTimeout(debounceTimer)
            debounceTimer = null
        }
        pending = false
    }

    const loadLocal = () => {
        try {
            const raw = localStorage.getItem(storageKey)
            if (!raw) return null
            const parsed = JSON.parse(raw)
            return parsed
        } catch (e) {
            return null
        }
    }

    return { notifyChange, forceSave, stop, loadLocal, isPending: () => pending }
}

// Legacy version renamed to avoid conflict
export function useDraftAutosaveLegacy(initialData = {}, intervalMs = 5000) {
    const draft = ref(initialData)
    let timer = null

    function startAutoSave(name = 'Auto draft') {
        stopAutoSave()
        timer = setInterval(async() => {
            try {
                await draftsAPI.save({ name, payload: JSON.stringify(draft.value) })
            } catch (e) {
                // ignore autosave failures
            }
        }, intervalMs)
    }

    function stopAutoSave() {
        if (timer) clearInterval(timer)
        timer = null
    }

    watch(draft, (val) => {
        // immediate localStorage backup
        try { localStorage.setItem('order_draft', JSON.stringify(val)) } catch (e) {}
    }, { deep: true })

    return { draft, startAutoSave, stopAutoSave }
}
