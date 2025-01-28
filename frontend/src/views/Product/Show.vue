<script setup>
import {ref, onMounted, computed} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useFetch } from '@/composables/useFetch';
import { formatPrice, getLocalStorage, productApiEndpoint } from '@/assets/js/helpers';
import { useCart } from '@/composables/useCart';
import { useSwal } from "@/composables/useSwal";

const router = useRouter();
const route = useRoute();
const { fetchJson } = useFetch();
const { addToCart } = useCart();
const { errorAlert } = useSwal();

const user = getLocalStorage('user');
const loading = ref(false);
const product = ref(null);

const isUserProduct = computed(() => user.id === product.value?.sellerId);

async function loadProduct() {
    if (!route.params.id || loading.value) return;

    loading.value = true;
    document.body.loader().show();

    try {
        const response = await fetchJson(productApiEndpoint(route.params.id));
        product.value = response;
    } catch (err) {
        errorAlert("Failed to load product");
        router.push({ name: 'home' });
    } finally {
        loading.value = false;
        document.body.loader().hide();
    }
}

async function handleAddToCart() {
    if (Number(product.value.stock) <= 0) return;

    addToCart(product.value).then((action) => {
        if (action === 'update') {
            successAlert(`Product quantity updated successfully.`);
        }
    });
}

onMounted(() => {
    loadProduct();
});
</script>

<template>
    <div class="container py-4 min-vh-100">
        <div v-if="product" class="row justify-content-center">
            <div class="col-12 col-lg-10">

                <div class="d-flex align-items-center mb-4">
                    <button class="btn btn-link text-dark p-0 me-3" @click="router.back()">
                        <i class="bi bi-arrow-left fs-4"></i>
                    </button>
                    <h1 class="h3 mb-0">Product Details</h1>
                </div>

                <div class="card bg-white border-0 shadow-sm rounded-4">
                    <div class="row g-0">

                        <div class="col-md-5">
                            <div class="bg-light d-flex align-items-center justify-content-center h-100 rounded-start-4" style="min-height: 400px;">
                                <div class="text-secondary">
                                    <i class="bi bi-image display-1"></i>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-7">
                            <div class="card-body p-4 p-lg-5">
                                <div class="d-flex justify-content-between align-items-start mb-3">
                                    <div>
                                        <h2 class="h3 mb-2">{{ product.name }}</h2>
                                        <div class="text-muted small">
                                            Reference: {{ product.reference }}
                                        </div>
                                    </div>
                                    <span class="badge bg-light text-dark border ms-2">
                                        {{ product.type }}
                                    </span>
                                </div>

                                <div class="mb-4">
                                    <h6 class="text-muted mb-2">Description</h6>
                                    <p class="mb-0">{{ product.description || 'No description available.' }}</p>
                                </div>

                                <div class="row g-3 mb-4">
                                    <div class="col-6">
                                        <div class="p-3 bg-light rounded-3">
                                            <div class="text-muted small mb-1">Price</div>
                                            <div class="h4 mb-0 fw-bold">{{ formatPrice(product.price) }}</div>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="p-3 bg-light rounded-3">
                                            <div class="text-muted small mb-1">Stock</div>
                                            <div class="h4 mb-0" :class="{'text-danger': product.stock === 0}">
                                                {{ product.stock }} units
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div v-if="!isUserProduct" class="d-grid gap-2">
                                    <button
                                        class="btn btn-dark py-3"
                                        @click="handleAddToCart"
                                        :disabled="product.stock === 0"
                                    >
                                        <i class="bi bi-cart-plus me-2"></i>
                                        Add to Cart
                                    </button>
                                </div>

                                <div v-else class="d-flex gap-2">
                                    <router-link
                                        :to="{ name: 'product.edit', params: { id: product.id }}"
                                        class="btn btn-dark flex-grow-1"
                                    >
                                        <i class="bi bi-pencil me-2"></i>
                                        Edit Product
                                    </router-link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</template>
