<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { userApiEndpoint } from "@/assets/js/helpers"
import { useFetch } from "@/composables/useFetch"

const router = useRouter()
const { fetchJson } = useFetch()

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
    
})
</script>

<template>
    <nav class="navbar navbar-expand-lg bg-body d-print-none">
        <div class="container">

            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mx-auto my-2">
                    <li class="nav-item fw-semibold mx-2">
                        <router-link class="nav-link" :to="{ name: 'stats.index' }">
                            <i class="bi bi-house me-2 font-size-1-2rem"></i> Home
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

</template>
