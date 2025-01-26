<script setup>
	defineProps({

		theadClasses: {
			type: String,
			default: 'table-dark'
		},

		contentClasses: {
			type: String,
			default: 'table border-bottom-none rounded-3 overflow-hidden mb-0'
		},
		
		tbodyClasses: {
			type: String,
			default: ''
		},
	})
</script>
<template>
	<div class="rounded-3 shadow-sm">
		<table :class="contentClasses">
	        <thead :class="theadClasses">
	            <slot name="thead"></slot>
	        </thead>
	        <tbody :class="tbodyClasses">
	            <slot name="tbody"></slot>
	        </tbody>
	    </table>
    </div>
</template>
