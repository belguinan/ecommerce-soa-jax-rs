<script setup>

import { useRouter } from 'vue-router'

const router = useRouter();

const props = defineProps({
    link: {
        type: String,
        default: '#'
    }
})


const goBack = () => {
    if (window.history.length > 2) {
        router.back()
    } else {
        router.push(props.link)
    }
}

</script>
<template>
    <router-link 
        :to="link"
        @click.prevent="goBack"
    >
        <slot />
    </router-link>
</template>