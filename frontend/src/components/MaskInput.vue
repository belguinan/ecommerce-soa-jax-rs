<script setup>
import { computed } from 'vue'
import { formatPhoneNumber } from '@/assets/js/helpers.js'

const props = defineProps({
    modelValue: {
        type: String,
        default: ''
    }
})

const emit = defineEmits(['update:modelValue'])

const displayValue = computed(() => {
    return formatPhoneNumber(props.modelValue || '')
})

const handleInput = (e) => {
    const digits = e.target.value.replace(/[^\d]/g, '')
    emit('update:modelValue', digits)
}
</script>

<template>
    <input
        type="tel"
        v-bind="$attrs"
        :value="displayValue"
        @input="handleInput"
    />
</template>