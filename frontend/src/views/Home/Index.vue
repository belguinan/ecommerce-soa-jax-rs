<script setup>
import { ref, onMounted } from 'vue';
import { PRODUCT_TYPES } from "@/assets/js/constants"
import { useFetch } from '@/composables/useFetch';
import { productApiEndpoint, getLocalStorage } from '@/assets/js/helpers';
import Pagination from '@/components/Pagination.vue';
import Product from '@/components/Product.vue';
import router from "@/router";
const { fetchJson } = useFetch();

const products = ref([]);
const loading = ref(true);
const error = ref(null);
const currentPage = ref(1);
const totalPages = ref(1);
const perPage = ref(12);
const sortBy = ref('id');
const sortOrder = ref('DESC');
const productTypes = ref(PRODUCT_TYPES);
const selectedType = ref('Any');
const viewMode = ref('all');
const currentUser = getLocalStorage('user');

async function fetchProducts() {
    loading.value = true;
    error.value = null;
    
    try {
        let endpoint = productApiEndpoint('') + 
            `?page=${currentPage.value}` +
            `&perPage=${perPage.value}` +
            `&sortBy=${sortBy.value}` +
            `&sortOrder=${sortOrder.value}`;

        if (selectedType.value !== 'Any') {
            endpoint += `&type=${selectedType.value}`;
        }

        if (viewMode.value === 'my-products') {
            endpoint += `&sellerId=${currentUser.id}`;
        }
        
        const response = await fetchJson(endpoint);
        products.value = response.data || [];
        totalPages.value = response.meta?.lastPage || 1;
    } catch (err) {
        error.value = "Error loading products.";
        console.error('Error fetching products:', err);
    } finally {
        loading.value = false;
    }
}

function handlePageChange(page) {
    currentPage.value = page;
    window.scrollTo({ top: 0, behavior: 'smooth' });
    fetchProducts();
}

function handleViewModeChange(mode) {
    viewMode.value = mode;
    currentPage.value = 1;
    fetchProducts();
}

function handleSortChange(field) {
    if (sortBy.value === field) {
        sortOrder.value = sortOrder.value === 'ASC' ? 'DESC' : 'ASC';
    } else {
        sortBy.value = field;
        sortOrder.value = 'DESC';
    }

    fetchProducts();
}

function handleViewProduct(product) {
    router.push({name: 'product.show', params: {id: product.id}});
}

onMounted(() => {
    fetchProducts();
});
</script>

<template>
    <div class="container-fluid min-vh-100">
        <div class="container py-4">
            <div class="row justify-content-between align-items-center g-4">
                <div class="col-12 col-lg-3">
                    <h1 class="display-6 fw-bold mb-0">Product Catalog</h1>
                    <p class="text-muted mt-2 mb-0">Discover our selection of quality products</p>
                </div>

                <div class="col-12 col-lg-9">
                    <div class="d-flex flex-wrap gap-3 justify-content-lg-end align-items-center">

                        <div class="btn-group shadow-sm flex-fill flex-lg-grow-0">
                            <button
                                type="button" 
                                class="btn py-2 px-4"
                                :class="viewMode === 'all' ? 'btn-dark' : 'btn-outline-dark'" 
                                @click="handleViewModeChange('all')"
                            >
                                All Products
                            </button>
                            <button 
                                type="button" 
                                class="btn py-2 px-4"
                                :class="viewMode === 'my-products' ? 'btn-dark' : 'btn-outline-dark'"
                                @click="handleViewModeChange('my-products')"
                            >
                                My Products
                            </button>
                        </div>

                        <div class="dropdown shadow-sm">
                            <button class="btn py-2 btn-dark border dropdown-toggle px-4" type="button" data-bs-toggle="dropdown">
                                <i class="bi bi-funnel me-2"></i>
                                Filter by type
                            </button>
                            <ul class="dropdown-menu">
                                <li v-for="type in productTypes" :key="type">
                                    <button 
                                        class="dropdown-item" 
                                        :class="{ active: selectedType === type }"
                                        @click="selectedType = type; fetchProducts()"
                                    >
                                        {{ type }}
                                    </button>
                                </li>
                            </ul>
                        </div>

                        <div class="dropdown shadow-sm">
                            <button class="btn py-2 btn-dark border dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                <i  
                                    class="bi me-2" 
                                    :class="sortOrder === 'ASC' ? 'bi-sort-up' : 'bi-sort-down'"></i>
                                Sort by
                            </button>
                            <ul class="dropdown-menu">
                                <li v-for="option in ['id', 'price', 'name', 'stock']" :key="option">
                                    <button 
                                        class="dropdown-item d-flex align-items-center justify-content-between"
                                        :class="{ active: sortBy === option }"
                                        @click="handleSortChange(option)"
                                    >
                                        <span class="text-capitalize">{{ option === 'id' ? 'Default' : option }}</span>
                                        <i 
                                            v-if="sortBy === option" 
                                            class="bi" 
                                            :class="sortOrder === 'ASC' ? 'bi-arrow-up' : 'bi-arrow-down'"
                                        ></i>
                                    </button>
                                </li>
                            </ul>
                        </div>

                        <router-link :to="{name: 'product.create'}" class="btn py-2 shadow-sm bg-white border-secondary px-4">
                            Create Product
                        </router-link>

                    </div>
                </div>
            </div>
        </div>

        <div class="container py-4">
            <div v-if="error" class="alert alert-danger" role="alert">
                {{ error }}
            </div>

            <div v-if="loading" class="d-flex justify-content-center py-5">
                <div class="spinner-border">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>

            <div v-else>
                <div class="row g-4">
                    <div v-for="(product, index) in products"
                         :key="product.id" 
                         class="col-sm-6 col-lg-4 col-xl-3"
                    >
                        <Product
                            :product="product"
                            @delete="products.splice(index, 1);"
                            @view="handleViewProduct(product)"
                        />
                    </div>
                </div>

                <div v-if="products.length === 0" class="text-center py-5">
                    <div class="bg-white rounded-4 shadow-sm p-5 mx-auto">
                        <i class="bi bi-inbox display-1 text-secondary mb-4 d-block"></i>
                        <h4 class="mb-3">No Products Found</h4>
                        <p class="text-muted mb-0">Try adjusting your search filters</p>
                    </div>
                </div>

                <div class="d-flex justify-content-end my-5" v-if="products.length > 0">
                    <Pagination 
                        :current-page="currentPage"
                        :total-pages="totalPages"
                        @page-change="handlePageChange"
                    />
                </div>
            </div>
        </div>
    </div>
</template>