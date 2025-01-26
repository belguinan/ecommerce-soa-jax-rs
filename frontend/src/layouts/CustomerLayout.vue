# src/layouts/CustomerLayout.vue
<script setup>
import { useRouter } from 'vue-router';
import ApplicationLogo from '@/components/ApplicationLogo.vue';
import {userApiEndpoint} from "@/assets/js/helpers";
import {useFetch} from "@/composables/useFetch";

const router = useRouter();
const { fetchJson } = useFetch();

async function handleLogout() {
    const endpoint = userApiEndpoint('logout');

    const response = await fetchJson(endpoint, {
        method: 'POST',
    });

    localStorage.removeItem('token');
    localStorage.removeItem('user');
    router.push({ name: 'login' });
}

</script>

<template>
    <nav class="navbar navbar-expand-lg bg-body">
        <div class="container">
            <!-- Logo -->
            <router-link class="navbar-brand align-items-center justify-content-center d-flex" :to="{ name: 'home' }">
                <ApplicationLogo class="img-fluid max-w-25px" />
            </router-link>

            <!-- Mobile Toggle -->
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <!-- Navigation Items -->
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{ name: 'home' }">
                            Home
                        </router-link>
                    </li>
<!--                    <li class="nav-item">-->
<!--                        <router-link class="nav-link" :to="{ name: 'products.index' }">-->
<!--                            Products-->
<!--                        </router-link>-->
<!--                    </li>-->
<!--                    <li class="nav-item">-->
<!--                        <router-link class="nav-link" :to="{ name: 'orders.index' }">-->
<!--                            My Orders-->
<!--                        </router-link>-->
<!--                    </li>-->
<!--                    <li class="nav-item">-->
<!--                        <router-link class="nav-link" :to="{ name: 'cart.index' }">-->
<!--                            Cart-->
<!--                        </router-link>-->
<!--                    </li>-->
                    <li class="nav-item">
                        <button class="btn btn-link nav-link" @click="handleLogout">
                            Logout
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="mt-4" id="mainContent">
        <router-view></router-view>
    </main>
</template>