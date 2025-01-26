<script setup>
	import CopyWrapper from '@/components/CopyWrapper.vue';
	
	defineProps({
		value: {
			required: false,
			default: null,
		},
		useCopy: {
			required: false,
			default: true,
		}
	})

	const phoneFormat = (phoneNumberString) => {
	    var cleaned = ('' + phoneNumberString).replace(/\D/g, '')
	    var match = cleaned.match(/^(1|)?(\d{3})(\d{3})(\d{4})$/)
	    if (match) {
	        return ['(', match[2], ') ', match[3], '-', match[4]].join('')
	    }
	    return phoneNumberString
	}
</script>

<template>
	<span>
		<CopyWrapper v-if="useCopy" :value="value === null ? '--' : phoneFormat(value)">
			{{ value === null ? '--' : phoneFormat(value) }}
		</CopyWrapper>
		<span v-else>
			{{ value === null ? '--' : phoneFormat(value) }}
		</span>
	</span>
</template>