<script setup>
import { computed } from 'vue';
import InputLabel from '@/components/InputLabel.vue';

const emit = defineEmits(['update:checked']);

const props = defineProps({
    error: {
        default: null,
        required: false
    },
    checked: {
        type: [Array, Boolean],
        required: true,
    },
    value: {
        default: null,
    },
});

const proxyChecked = computed({
    get() {
        return props.checked;
    },

    set(val) {
        emit('update:checked', val);
    },
});
</script>

<template>
    <InputLabel :class="{'text-danger': error}">
        <input
            :class="{'border-danger': error}"
            class="form-check-input"
            type="checkbox"
            :value="value"
            v-model="proxyChecked"
        />
        <slot />
    </InputLabel>
</template>
