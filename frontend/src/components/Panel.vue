<script setup>

	import PanelLabel from './PanelLabel.vue'

	defineProps({
		contentClasses: {
			type: String,
			required: false,
			default: 'p-4 rounded-4 bg-white',
		},
		label: {
			type: String,
			required: false,
			default: '',
		}
	})
</script>

<template>
	<div :class="contentClasses">
		<PanelLabel v-if="label !== ''" :value="label" />

		<slot />
	</div>
</template>