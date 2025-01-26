<script setup>
	defineProps({
		links: Object,
		only: Array,
		preserveState: {
			type: Boolean,
			default: false
		},
	})
</script>
<template>
	<Transition name="opacity">
		<ul class="pagination" v-if="links?.length > 0">
			<li 
				v-for="link in links" 
				class="page-item shadow-sm" 
			>
				<a 
					prefetch
					class="page-link" 
					:class="{'active': link.active}" 
					:href="link.url === null ? '#' : link.url"
					:only="only"
					:preserve-state="preserveState"
				>
					<span v-html="link.label"></span>
				</a>
			</li>
		</ul>
	</Transition>
</template>