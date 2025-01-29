<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useFetch } from '@/composables/useFetch'
import { formatPrice, orderApiEndpoint } from '@/assets/js/helpers'
import { useSwal } from "@/composables/useSwal"

const router = useRouter()
const route = useRoute()
const { fetchJson } = useFetch()
const { errorAlert } = useSwal()

const loading = ref(false)
const order = ref(null)

async function loadOrder() {

    if (!route.params.id || loading.value) return

    loading.value = true
    document.body.loader().show()

    try {
        const response = await fetchJson(orderApiEndpoint(route.params.id))

        if (response.status === 404) {
            throw new Error()
        }
        
        order.value = response
    } catch (err) {
        document.body.loader().hide()
        errorAlert("Failed to load order details")
        router.push({ name: 'home' })
    } finally {
        loading.value = false
        document.body.loader().hide()
    }
}

onMounted(() => {
    loadOrder()
})

function print() {
    window.print()
}
</script>

<template>
    <div class="container py-4 min-vh-100">
        <div v-if="order" class="row justify-content-center">
            <div class="col-12 col-lg-10">

                <div class="d-flex align-items-center mb-4">
                    <button class="btn btn-link text-dark p-0 me-3 d-print-none" @click="router.back()">
                        <i class="bi bi-arrow-left fs-4"></i>
                    </button>
                    <h1 class="h3 mb-0">Order Details</h1>
                </div>

                <div class="card bg-white border-0 shadow-sm rounded-4 mt-5">
                    <div class="card-body p-4 p-lg-5">
                        <div class="row g-4">
                            <div class="col-12">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h4 class="mb-2">Order #{{ order.id }}</h4>
                                        <div class="text-muted small">
                                            {{ new Date(order.createdAt).toLocaleDateString() }}
                                        </div>
                                    </div>
                                    <span class="badge bg-success bg-opacity-10 text-success px-3 py-2 rounded-3">
                                        {{ order.status }}
                                    </span>
                                </div>
                            </div>

                            <div class="col-12">
                                <hr class="text-muted opacity-50">
                            </div>

                            <div class="col-md-12">
                                <h6 class="text-muted mb-3">Shipping Information</h6>
                                <div class="bg-light rounded-3 p-4">
                                    <h6 class="fw-bold mb-2">{{ order.name }}</h6>
                                    <div class="text-muted mb-1">{{ order.address }}</div>
                                    <div class="text-muted mb-1">{{ order.city }}</div>
                                    <div class="text-muted">{{ order.phoneNumber }}</div>
                                </div>
                            </div>

                            <div class="col-12">
                                <hr class="text-muted opacity-50">
                            </div>

                            <div class="col-12">
                                <h6 class="text-muted mb-3">Order Items</h6>
                                <div class="table-responsive">
                                    <table class="table table-borderless">
                                        <thead class="text-muted">
                                            <tr>
                                                <th>Item</th>
                                                <th class="text-center">Quantity</th>
                                                <th class="text-end">Price</th>
                                                <th class="text-end">Total</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="item in order.items" :key="item.id">
                                                <td>
                                                    <h6 class="mb-1">{{ item.product.name }}</h6>
                                                    <small class="text-muted">{{ item.product.type }}</small>
                                                </td>
                                                <td class="text-center">{{ item.quantity }}</td>
                                                <td class="text-end">{{ formatPrice(item.price) }}</td>
                                                <td class="text-end">{{ formatPrice(item.total) }}</td>
                                            </tr>
                                        </tbody>
                                        <tfoot class="border-top">
                                            <tr>
                                                <td colspan="3" class="text-end fw-bold">Subtotal</td>
                                                <td class="text-end">{{ formatPrice(order.total) }}</td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" class="text-end fw-bold">Shipping</td>
                                                <td class="text-end">{{ formatPrice(0) }}</td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" class="text-end fw-bold h5">Total</td>
                                                <td class="text-end fw-bold h5">{{ formatPrice(order.total) }}</td>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div>
                            </div>

                            <div class="col-12">
                                <hr class="text-muted opacity-50">
                            </div>

                            <div class="col-12 text-center d-print-none">
                                <button class="btn btn-light border me-2" @click="print">
                                    <i class="bi bi-printer me-2"></i>
                                    Print Order
                                </button>
                                <button class="btn btn-dark" @click="router.push({name: 'home'})">
                                    Continue Shopping
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>