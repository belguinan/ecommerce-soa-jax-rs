<script setup>
import { formatPrice, getLocalStorage, productApiEndpoint } from '@/assets/js/helpers';
import { useSwal } from "@/composables/useSwal";
import { useFetch } from "@/composables/useFetch";
import { useCart } from '@/composables/useCart'

const user = getLocalStorage('user');

const { addToCart } = useCart()

const props = defineProps({
    product: {
        type: Object,
        required: true
    }
});

const { fetchJson } = useFetch();
const { errorAlert, successAlert, confirm } = useSwal();
const emit = defineEmits(['delete', 'view']);

const isUserProduct = user.id == props.product.sellerId;

function handleAddToCart() {
    if (Number(props.product.stock) <= 0) {
        return;
    }

    addToCart(props.product).then((action) => {
        if (action === 'update') {
            successAlert(`Product quantity updated successfully.`);
        }
    }).catch(err => {
        errorAlert("Something went wrong!")
    });
}

function handleDelete() {
    return confirm("Please confirm this action", {
        icon: "warning"
    }).then(result => {
        if (result.isConfirmed) {
            fetchJson(productApiEndpoint(`/${props.product.id}`), {
                method: "DELETE",
            }).then(result => {
                successAlert("Product deleted successfully.").then(() => {
                    emit("delete");
                });
            }).catch(error => {
                errorAlert("Something went wrong");
            })
        }
    })
}

</script>

<template>
    <div
        class="border-secondary card h-100 border rounded-4 shadow-sm overflow-hidden product-card position-relative"
        :class="{
            'bg-white': product.stock > 0,
            'bg-danger bg-opacity-10': product.stock === 0
        }"
    >
        
        <div v-if="isUserProduct" class="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center product-actions z-1">
            <div class="d-flex gap-2">
                <button
                    @click.stop="handleDelete"
                    class="btn btn-dark p-0 d-flex align-items-center justify-content-center w-40px h-40px rounded-circle shadow-sm"
                >
                    <i class="bi bi-trash"></i>
                </button>

                <router-link class="btn btn-dark p-0 d-flex align-items-center justify-content-center w-40px h-40px rounded-circle shadow-sm" :to="{ name: 'product.edit', params: {id: product.id} }">
                    <i class="bi bi-pencil"></i>
                </router-link>

                <router-link class="btn btn-dark p-0 d-flex align-items-center justify-content-center w-40px h-40px rounded-circle shadow-sm" :to="{ name: 'product.show', params: {id: product.id} }">
                    <i class="bi bi-eye"></i>
                </router-link>
            </div>
        </div>
        
        <div
            class="bg-light d-flex align-items-center justify-content-center h-250px position-relative product-image cursor-pointer"
            @click.stop="emit('view', product)"
        >
            <div class="text-secondary">
                <i class="bi bi-image display-4"></i>
            </div>
        </div>

        <div class="card-body p-4 product-content">
            <div class="d-flex justify-content-between align-items-start mb-2">
                <h5 class="card-title text-truncate mb-0">
                    {{ product.name }}
                </h5>
                <span class="badge bg-light text-dark border ms-2">
                    {{ product.type }}
                </span>
            </div>

            <p class="card-text small text-muted">
                {{ product.description?.substring(0, 80) }}
                <span v-if="product.description?.length > 80">...</span>
            </p>

            <div class="d-flex justify-content-between align-items-center mt-4">
                <div class="d-flex flex-column">
                    <span class="h5 mb-0 fw-bold">{{ formatPrice(product.price) }}</span>
                    <small class="text-muted" v-if="product.stock > 0">
                        Stock: {{ product.stock }}
                    </small>
                    <span v-else class="fw-bold text-danger">
                        Out of stock
                    </span>
                </div>
                <div class="btn-group" v-if="!isUserProduct">
                    <button
                        class="btn btn-dark btn-sm rounded-3 py-2 px-3"
                        @click.stop="handleAddToCart(product)"
                        :disabled="product.stock === 0"
                    >
                        <i class="bi bi-cart-plus me-2"></i>
                        Add to Cart
                    </button>
                </div>
                <button
                    v-else 
                    class="btn btn-light border btn-sm rounded-3 px-3"
                    disabled
                >
                    <i class="bi bi-box me-2"></i>
                    Your Product
                </button>
            </div>
        </div>
    </div>
</template>

