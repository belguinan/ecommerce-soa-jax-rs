<script setup>
import { onMounted, ref } from 'vue';

const model = defineModel({
    // type: String,
    required: true,
});

const input = ref(null);

const props = defineProps({
    error: {
        default: null,
        required: false
    }
})

onMounted(() => {
    if (input.value.hasAttribute('autofocus')) {
        input.value.focus();
    }
});

defineExpose({ focus: () => input.value.focus() });

</script>

<template>
    <input
        :class="{'border-danger': error}"
        class="form-control"
        type="text"
        v-model="model"
        ref="input"
    />
</template>
