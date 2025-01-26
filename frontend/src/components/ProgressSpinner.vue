<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue';

const spinner = ref(null)
const animationLoop = ref(null)
const degrees = ref(0);
const currentDegree = ref(0)

const props = defineProps({
	value: Number,
});

function init(width, height) {
	let ctx = spinner.value.getContext("2d");
	ctx.clearRect(0, 0, width, height);
	ctx.beginPath();
	ctx.strokeStyle = "#e7e7e7";
	ctx.lineWidth = 15;
	ctx.arc(width/2, width/2, 32, 0, Math.PI*2, false);
	ctx.stroke();
	let radians = currentDegree.value * Math.PI / 180;

	ctx.beginPath();
	ctx.strokeStyle = "#212529";
	ctx.lineWidth = 15;
	ctx.arc(width/2, height/2, 32, 0 - 90*Math.PI/180, radians - 90*Math.PI/180, false); 
	ctx.stroke();
	ctx.font = "15px Roboto";
	let text = Math.floor(currentDegree.value/360*100);

	if (text > 100) {
		text = 100;
	}

	if (text <= 10) {
		ctx.fillStyle = "#7c7c7c";
	} else if (text <= 20) {
		ctx.fillStyle = "#2a2a2a";
	} else if (text > 50) {
		ctx.fillStyle = "#212529";
	}

	let text_width = ctx.measureText(text + "%").width;
	ctx.fillText(text + "%", width/2 - text_width/2, height/2 + 6.45);
}

function animateTo() {
	if(currentDegree.value == degrees.value)  {
		clearInterval(animationLoop.value);
	} else if(currentDegree.value < degrees.value) {
		currentDegree.value++;
	} else {
		currentDegree.value--;
	}

	init(spinner.value.width, spinner.value.height);
}

function draw(currentValue, interval) {

	if (isNaN(currentValue)) {
		show.value = false;
		return;
	}

	degrees.value = parseInt(currentValue);

	interval = interval || (10000/(degrees - currentDegree.value));

	if (isNaN(interval)) {
		interval = 0;
	}

	if (typeof animationLoop.value != null)  {
		clearInterval(animationLoop.value);
	}

	animateTo();

	animationLoop.value = setInterval(animateTo, interval);
}

watch(() => props.value, newValue => {
	newValue = parseInt(newValue);
	if ((! isNaN(newValue)) && newValue > 0) {
		draw(360 * newValue / 100, 5);
	}
})

onMounted(() => {
	draw(360 * props.value / 100, 0)
})

onUnmounted(() => {
	clearInterval(animationLoop.value);
})
</script>

<template>
	<canvas ref="spinner" />
</template>