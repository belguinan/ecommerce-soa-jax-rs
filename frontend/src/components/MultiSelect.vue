<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
    options: {
        type: Array,
        required: true,
        default: () => []
    },
    modelValue: {
        type: Array,
        required: true,
        default: () => []
    },
    label: {
        type: String,
        default: ''
    },
    id: {
        type: String,
        default: 'multiselect'
    },
    displaySelected: {
        type: Boolean,
        default: false,
    },
    searchable: {
        type: Boolean,
        default: true
    },
    placeholder: {
        type: String,
        default: 'Select items'
    }
})

const emit = defineEmits(['update:modelValue'])

const isOpen = ref(false)
const searchQuery = ref('')
const dropdownRef = ref(null)

const filteredOptions = computed(() => {
    if (!searchQuery.value) return props.options
    return props.options.filter(option =>
        option.label.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
})

const selectedOptions = computed(() =>
    props.options.filter(option => isSelected(option))
)

const selectedText = computed(() => {
    if (props.modelValue.length === 0) return props.placeholder
    if (props.modelValue.length === 1) return selectedOptions.value[0]?.label
    return `${props.modelValue.length} items selected`
})

const isSelected = (option) => props.modelValue.includes(option.value) || props.modelValue.includes(String(option.value))

const toggleSelection = (option) => {
    const newValue = [...props.modelValue]
    let index = newValue.indexOf(option.value)

    if (index === -1) {
        index = newValue.indexOf(String(option.value))
    }

    if (index === -1) {
        newValue.push(option.value)
    } else {
        newValue.splice(index, 1)
    }

    emit('update:modelValue', newValue)
}

const toggleDropdown = (event) => {
    event.preventDefault()
    isOpen.value = !isOpen.value
}

const handleClickOutside = (event) => {
    if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
        isOpen.value = false
    }
}

onMounted(() => {
    document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
    document.removeEventListener('click', handleClickOutside)
})
</script>

<template>
    <div class="form-group">

        <label v-if="label" :for="id" class="form-label font-size-0-9rem">{{ label }}</label>

        <div class="dropdown" ref="dropdownRef">
            <button
                :id="id"
                class="btn btn-sm border btn-outline-dark dropdown-toggle w-100 d-flex justify-content-between align-items-center"
                type="button"
                @click="toggleDropdown"
            >
                <span class="me-2">{{ selectedText }}</span>
            </button>

            <ul
                v-show="isOpen"
                class="dropdown-menu w-100 max-h-300px overflow-auto shadow-sm show"
                style="max-height: 300px;"
                @click.stop
            >
                <li v-if="searchable">
                    <input
                        type="text"
                        class="form-control form-control-sm mx-2 my-2"
                        style="width: 95%"
                        v-model="searchQuery"
                        placeholder="Search..."
                        @click.stop
                    >
                </li>
                <li v-for="option in filteredOptions" :key="option.value">
                    <div class="dropdown-item">
                        <div class="form-check">
                            <input
                                type="checkbox"
                                class="form-check-input"
                                :id="`${id}-${option.value}`"
                                :checked="isSelected(option)"
                                @change="toggleSelection(option)"
                                @click.stop
                            >
                            <label
                                class="form-check-label"
                                :for="`${id}-${option.value}`"
                                @click.prevent.stop="toggleSelection(option)"
                            >
                                {{ option.label }}
                            </label>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

        <div class="mt-2 d-flex flex-wrap gap-2" v-if="displaySelected">
            <span
                v-for="option in selectedOptions"
                :key="option.value"
                class="badge bg-primary d-flex align-items-center"
            >
                {{ option.label }}
                <button
                    type="button"
                    class="btn-close btn-close-white ms-2"
                    style="font-size: 0.5rem"
                    @click.stop="toggleSelection(option)"
                ></button>
            </span>
        </div>
    </div>
</template>