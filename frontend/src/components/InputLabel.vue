<script setup>
defineProps({
    value: {
        type: String,
    },
});
</script>

<template>
    <label class="form-label text-capitalize">
        <span v-if="value">{{ value }}</span>
        <span v-else><slot /></span>
    </label>
</template>
