<script setup>
import { ref, onMounted } from 'vue';
import { PRODUCT_TYPES } from "@/assets/js/constants"
import { useRouter, useRoute } from 'vue-router';
import { useFetch } from '@/composables/useFetch';
import { getLocalStorage, productApiEndpoint } from '@/assets/js/helpers';
import { useSwal } from "@/composables/useSwal";

const router = useRouter();
const route = useRoute();
const { fetchJson } = useFetch();
const { errorAlert, successAlert } = useSwal();

const user = getLocalStorage('user');
const loading = ref(false);
const productTypes = ref(PRODUCT_TYPES);

const product = ref({
    name: '',
    type: '',
    description: '',
    reference: '',
    price: '',
    stock: ''
});

const errorResponse = ref({});

async function loadProduct() {
    if (!route.params.id || loading.value) return;

    document.body.loader().show();
    loading.value = true;

    try {
        const response = await fetchJson(productApiEndpoint(route.params.id));
        product.value = response;

        if (response.sellerId !== user.id) {
            router.push({ name: 'home' });
            return;
        }
    } catch (err) {
        console.log(err);
        errorAlert("Failed to load product");
    } finally {
        loading.value = false;
        document.body.loader().hide();
    }
}

async function handleSubmit() {
    if (loading.value) return;

    document.body.loader().show();
    loading.value = true;
    errorResponse.value = {};

    try {
        const isEdit = route.params.id;
        const endpoint = productApiEndpoint(isEdit ? route.params.id : '');
        const method = isEdit ? 'PUT' : 'POST';

        const response = await fetchJson(endpoint, {
            method,
            body: product.value
        });

        if (response.errors) {
            errorResponse.value = response;
            return;
        }

        const message = isEdit ? 
            "Product has been updated successfully." : 
            "Product has been created successfully.";

        successAlert(message).then(() => {
            router.push({ 
                name: 'home', 
                query: { 
                    view: 'my-products', 
                    sortBy: 'id', 
                    sortOrder: 'desc' 
                } 
            });
        });
    } catch (err) {
        const action = route.params.id ? 'update' : 'create';
        errorAlert(`Failed to ${action} this product`);
        console.error(err);
    } finally {
        document.body.loader().hide();
        loading.value = false;
    }
}

onMounted(() => {
    if (route.params.id) {
        loadProduct();
    }
});
</script>

<template>
    <div class="container py-4 min-vh-100">
            <div class="row justify-content-center">
                <div class="col-12 col-lg-8">
                    <div class="d-flex align-items-center mb-4">
                        <button
                            class="btn btn-link text-dark p-0 me-3"
                            @click="router.back()"
                        >
                            <i class="bi bi-arrow-left fs-4"></i>
                        </button>
                        <h1 class="h3 mb-0">
                            {{ route.params.id ? 'Edit' : 'Create' }} Product
                        </h1>
                    </div>

                    <div class="card bg-white border-0 shadow-sm rounded-4 p-2">
                        <div class="card-body p-4">
                            <form @submit.prevent="handleSubmit" class="row g-4">
                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.name}">
                                        <input
                                            type="text"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.name}"
                                            v-model="product.name"
                                            id="name"
                                            placeholder="Product name"
                                            required
                                        >
                                        <label for="name">Product Name</label>
                                        <div v-if="errorResponse?.errors?.name" class="invalid-feedback">
                                            {{ errorResponse.errors.name }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.type}">
                                        <select
                                            class="form-select bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.type}"
                                            v-model="product.type"
                                            id="type"
                                            required
                                        >
                                            <option value="">Select type</option>
                                            <option
                                                v-for="type in productTypes"
                                                :key="type"
                                                :value="type"
                                            >
                                                {{ type }}
                                            </option>
                                        </select>
                                        <label for="type">Type</label>
                                        <div v-if="errorResponse?.errors?.type" class="invalid-feedback">
                                            {{ errorResponse.errors.type }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.reference}">
                                        <input
                                            type="text"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.reference}"
                                            v-model="product.reference"
                                            id="reference"
                                            placeholder="Product reference"
                                            required
                                        >
                                        <label for="reference">Reference</label>
                                        <div v-if="errorResponse?.errors?.reference" class="invalid-feedback">
                                            {{ errorResponse.errors.reference }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.price}">
                                        <input
                                            type="number"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.price}"
                                            v-model="product.price"
                                            id="price"
                                            min="0"
                                            step="0.01"
                                            placeholder="0.00"
                                            required
                                        >
                                        <label for="price">Price ($)</label>
                                        <div v-if="errorResponse?.errors?.price" class="invalid-feedback">
                                            {{ errorResponse.errors.price }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.stock}">
                                        <input
                                            type="number"
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.stock}"
                                            v-model="product.stock"
                                            id="stock"
                                            min="0"
                                            placeholder="Available quantity"
                                            required
                                        >
                                        <label for="stock">Stock</label>
                                        <div v-if="errorResponse?.errors?.stock" class="invalid-feedback">
                                            {{ errorResponse.errors.stock }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <div class="form-floating" :class="{'is-invalid': errorResponse?.errors?.description}">
                                        <textarea
                                            class="form-control bg-light"
                                            :class="{'is-invalid': errorResponse?.errors?.description}"
                                            v-model="product.description"
                                            id="description"
                                            placeholder="Product description"
                                            style="height: 120px"
                                        ></textarea>
                                        <label for="description">Description</label>
                                        <div v-if="errorResponse?.errors?.description" class="invalid-feedback">
                                            {{ errorResponse.errors.description }}
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12 gap-2 text-end">
                                    <div class="btn-group">
                                        <button
                                            type="button"
                                            class="btn bg-dark-subtle px-4 fw-semibold"
                                            @click="router.back()"
                                        >
                                            Cancel
                                        </button>
                                        <button
                                            type="submit"
                                            class="btn btn-dark px-4 fw-semibold"
                                            :disabled="loading"
                                        >
                                            <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                                            Submit
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</template>
