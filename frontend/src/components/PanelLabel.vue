<script setup>
	defineProps({
		value: {
			type: String,
			default: ''
		}
	})
</script>
<template>
	<h3 class="mb-3 small font-size-0-85rem font-family-inter text-uppercase">{{ value }}</h3>
</template>