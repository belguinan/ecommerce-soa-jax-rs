import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './assets/js/loader.js'
import './assets/sass/app.scss';

import VueSweetalert2 from 'vue-sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

import { Dropdown } from 'bootstrap/js/index.esm.js'

const app = createApp(App);

app
	.use(router)
	.use(VueSweetalert2)
	.mount('#app');