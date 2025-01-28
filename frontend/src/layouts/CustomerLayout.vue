<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCart } from '@/composables/useCart'
import ApplicationLogo from '@/components/ApplicationLogo.vue'
import CartModal from '@/components/CartModal.vue'
import { userApiEndpoint } from "@/assets/js/helpers"
import { useFetch } from "@/composables/useFetch"

const router = useRouter()
const { fetchJson } = useFetch()
const { cartItemCount, showCartModal, loadCart } = useCart()

isLoggedIn().then(res => {
    if (! res) {
        handleLogout()
    }
}).catch(error => handleLogout())

async function handleLogout() {
    try {
        const endpoint = userApiEndpoint('logout')
        await fetchJson(endpoint, { method: 'POST' })
    } catch (e) {}
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    localStorage.removeItem('cart')
    router.push({ name: 'login' })
}

function isLoggedIn() {
    return new Promise((resolve, reject) => {
        const endpoint = userApiEndpoint('is-logged-in');
        fetchJson(endpoint, {
            method: 'POST',
        }).then(response => {
            if (response.status === 401) {
                reject(false)
            }
            resolve(true)
        }).catch(() => reject(false));
    })
}

onMounted(() => {
    loadCart()
})
</script>

<template>
    <nav class="navbar navbar-expand-lg bg-body">
        <div class="container">
            <router-link class="navbar-brand align-items-center justify-content-center d-flex" :to="{ name: 'home' }">
                <ApplicationLogo class="img-fluid max-w-25px" />
            </router-link>

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item fw-semibold mx-2">
                        <router-link class="nav-link" :to="{ name: 'home' }">
                            <i class="bi bi-house me-2 font-size-1-2rem"></i> Home
                        </router-link>
                    </li>
                    <li class="nav-item fw-semibold mx-2">
                        <button
                            @click="showCartModal = true"
                            class="nav-link position-relative"
                        >
                            <i class="bi bi-cart3 me-2 font-size-1-2rem"></i>
                            Cart
                            <span
                                v-if="cartItemCount > 0"
                                class="position-absolute translate-middle badge rounded-4 bg-danger bg-opacity-75"
                                style="top: 8px; right: -18px;"
                            >
                                {{ cartItemCount }}
                            </span>
                        </button>
                    </li>
                    <li class="nav-item fw-semibold mx-2">
                        <router-link class="nav-link" :to="{ name: 'profile.edit' }">
                            <i class="bi bi-person me-2 font-size-1-2rem"></i>
                            Profile
                        </router-link>
                    </li>
                    <li class="nav-item fw-semibold mx-2">
                        <button class="nav-link" @click="handleLogout">
                            <i class="bi bi-box-arrow-right me-2 font-size-1-2rem"></i> Logout
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="mt-4" id="mainContent">
        <router-view></router-view>
    </main>

    <CartModal />

</template>
