<script setup>
import { useSwal } from '@/composables/useSwal.js'

const props = defineProps({
	button: {
		required: false,
		default: false,
		type: Boolean
	},
    value: {
    	required: true
    },
    imgName: {
    	tyle: String,
    	default: 'copy.svg'
    },
});

const { successAlert, errorAlert, confirm } = useSwal();

function fallbackCopyTextToClipboard(text) {
	var textArea = document.createElement("textarea");
	textArea.value = text;

	// Avoid scrolling to bottom
	textArea.style.top = "0";
	textArea.style.left = "0";
	textArea.style.position = "fixed";

	document.body.appendChild(textArea);
	textArea.focus();
	textArea.select();

	try {
		var successful = document.execCommand('copy');
		var msg = successful ? 'successful' : 'unsuccessful';
		console.log('Fallback: Copying text command was ' + msg);
	} catch (err) {
		console.error('Fallback: Oops, unable to copy', err);
	}

  	document.body.removeChild(textArea);
}

function copyTextToClipboard(text) {
  	if (! navigator.clipboard) {
    	fallbackCopyTextToClipboard(text);
    	return;
  	}
  	navigator.clipboard.writeText(text).then(function() {
    	successAlert(`Copied to clipboard successfully`)
  	}, function(err) {
    	errorAlert(`Failed to copy the text`)
  	});
}

</script>

<template>
    <kbd class="small overflow-scroll text-nowrap position-relative d-flex flex-row flex-nowrap justify-content-between" v-if="! button">
    	<span class="overflow-scroll">
	    	{{ value }}
    	</span>
    	<span>
    		<img @click="copyTextToClipboard(value)" :src="`/images/${imgName}`" class="z-1" style="cursor: pointer;" width="20" height="20">
    	</span>
    </kbd>
    
    <img v-else @click="copyTextToClipboard(value)" :src="`/images/${imgName}`" class="z-1" style="cursor: pointer;" width="20" height="20">
</template>
