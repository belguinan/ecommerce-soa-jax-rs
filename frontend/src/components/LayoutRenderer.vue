<template>
    <component 
        v-if="layout" 
        :is="layout"
    >
        <slot />
    </component>
    <slot v-else />
</template>

<script setup>
import { useLayout } from '@/composables/useLayout'

const { layout } = useLayout()
</script>