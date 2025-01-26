<script setup>
import { ref, onMounted } from 'vue';
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark.min.css';

const props = defineProps({
	value: {
		default: ''
	}
})

const element = ref(null)

onMounted(() => {
	hljs.highlightElement(element.value)
	
})

</script>
<template>
	<pre ref="element">{{ value }}</pre>
</template>