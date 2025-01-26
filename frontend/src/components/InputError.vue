<script setup>
defineProps({
    message: {
        type: String,
    },
});
</script>

<template>
    <Transition name="opacity">
        <div v-show="message">
            <p class="small text-danger text-capitalize">
                <span v-if="typeof message === 'object'">
                    {{ message[0] }}
                </span>
                <span v-else>
                    {{ message }}
                </span>
            </p>
        </div>
    </Transition>
</template>
