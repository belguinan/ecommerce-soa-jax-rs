<script setup>
    import LayoutRenderer from '@/components/LayoutRenderer.vue'
</script>

<template>
    <LayoutRenderer>
        <router-view></router-view>
    </LayoutRenderer>
</template>


<style>
body {
    margin: 0;
    min-height: 100vh;
}

#app {
    min-height: 100vh;
}
</style>