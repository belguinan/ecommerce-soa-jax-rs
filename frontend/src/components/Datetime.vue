<script setup>
import { ref, onMounted } from 'vue'

const date = ref('')

const props = defineProps({
	value: String,
	format: {
		type: String,
		default: 'm/d h:mm A'
	}
})

onMounted(() => {

	const dateVal = new Date(props.value);

	const dateValues = new Map();
	dateValues.set('Y', dateVal.getFullYear())
	dateValues.set('m', dateVal.getMonth()+1)
	dateValues.set('d', dateVal.getDate())
	dateValues.set('h', dateVal.getHours() > 12 ? dateVal.getHours() - 12 : dateVal.getHours())
	dateValues.set('A', dateVal.getHours() > 12 ? 'PM' : 'AM')
	dateValues.set('H', dateVal.getHours())
	dateValues.set('mm', dateVal.getMinutes())
	dateValues.set('s', dateVal.getSeconds())

	try {
		date.value = props.format;

		[...props.format.matchAll(/\w+/g)].map(arr => {
			let v = arr[0]
			if (dateValues.get(v) !== undefined) {
				let value = dateValues.get(v)
				value = Number(value) < 10 ? `0${value}` : value
				date.value = date.value.replace(v, String(value))
			}
		})
	} catch (e) {
		date.value = props.value
	}
})

</script>

<template>
	<span>{{ date }}</span>
</template>