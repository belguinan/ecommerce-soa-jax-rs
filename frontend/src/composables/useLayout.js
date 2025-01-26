import { ref, markRaw } from 'vue'

const layout = ref(null)

export function useLayout() {
    const setLayout = (newLayout) => {
        layout.value = markRaw(newLayout)
    }

    return {
        layout,
        setLayout
    }
}