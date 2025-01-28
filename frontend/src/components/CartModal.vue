<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { formatPrice } from '@/assets/js/helpers'
import { useCart } from '@/composables/useCart'
import { useSwal } from "@/composables/useSwal";
import { useFetch } from "@/composables/useFetch";

const { 
    cartItems, 
    showCartModal,
    updateCartItemQuantity, 
    removeFromCart 
} = useCart()

const { errorAlert } = useSwal()

const modal = ref(null)
let bsModal = null

const total = computed(() => {
    return cartItems.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity)
    }, 0)
})

const { confirm } = useSwal()

function handleQuantityUpdate(item, newQuantity) {
    if (newQuantity < 1 || newQuantity > item.stock) return
    updateCartItemQuantity(item.id, newQuantity).catch(err => {
        errorAlert("Something went wrong!")
    })
}

function handleRemoveItem(itemId) {
    return confirm('Are you sure you want to remove this item?', {
        icon: "warning"
    }).then(result => {
        if (result.isConfirmed) {
            removeFromCart(itemId)
        }
    })
}

function handleCheckout() {
    console.log(cartItems.value)
}

watch(showCartModal, (newValue) => {
    if (bsModal) {
        newValue ? bsModal.show() : bsModal.hide()
    }
})

onMounted(() => {
    try {
        bsModal = window.Modal.getOrCreateInstance(modal.value)
        modal.value.addEventListener('hidden.bs.modal', () => {
            showCartModal.value = false
        })

        getCart();
    } catch (e) {}
})

onUnmounted(() => {
    try {
        modal.value.removeEventListener('hidden.bs.modal')
    } catch (e) {}
})
</script>

<template>
    <div class="modal fade" tabindex="-1" ref="modal" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-dialog-centered max-w-600px">
            <div class="modal-content bg-white rounded-4 p-2">
                <div class="modal-header border-bottom-0">
                    <h5 class="modal-title">
                        <span v-if="cartItems.length > 0">
                            <i class="bi bi-cart3 me-2"></i>
                            Shopping Cart
                        </span>
                    </h5>
                    <button 
                        type="button" 
                        class="btn-close" 
                        @click="showCartModal = false" 
                        aria-label="Close"
                    ></button>
                </div>

                <div class="modal-body p-0">

                    <div v-if="cartItems.length === 0" class="text-center p-4">
                        <i class="bi bi-cart-x display-4"></i>
                        <p class="mt-3 mb-0">Your cart is empty</p>
                    </div>

                    <div class="p-3">
                        <div class="list-group rounded-4">
                            <div v-for="item in cartItems"
                                 :key="item.id"
                                 class="list-group-item bg-body p-4"
                            >

                                <div class="d-flex w-100 justify-content-between mb-3">
                                    <h6 class="mb-0 fw-semibold text-capitalize">{{ item.name }}</h6>
                                    <div class="d-flex gap-3 align-items-start">
                                        <button
                                            class="btn-close"
                                            @click="handleRemoveItem(item.id)"
                                        ></button>
                                    </div>
                                </div>

                                <div class="d-flex justify-content-between align-items-center pt-2">
                                    <div class="fst-italic fw-semibold">
                                        {{ formatPrice(item.price) }} × {{ item.quantity }}
                                        <span class="ms-2">=</span>
                                        <strong class="ms-2">{{ formatPrice(item.price * item.quantity) }}</strong>
                                    </div>

                                    <div class="btn-group">
                                        <button
                                            class="btn btn-dark btn-sm"
                                            @click="handleQuantityUpdate(item, item.quantity - 1)"
                                            :disabled="item.quantity <= 1"
                                        >
                                            <i class="bi bi-dash"></i>
                                        </button>
                                        <button class="btn btn-dark btn-sm px-4" disabled>
                                            {{ item.quantity }}
                                        </button>
                                        <button
                                            class="btn btn-dark btn-sm"
                                            @click="handleQuantityUpdate(item, item.quantity + 1)"
                                            :disabled="item.quantity >= item.stock"
                                        >
                                            <i class="bi bi-plus"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <div v-if="cartItems.length > 0" class="p-3">
                        <div class="d-flex justify-content-end align-items-center">
                            <h5 class="mb-0 px-3 py-2 bg-body border rounded-3">
                                <span class="fw-semibold">Total:</span> <span class="fw-bold">{{ formatPrice(total) }}</span>
                            </h5>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-top-0" v-if="cartItems.length > 0">
                    <button
                        type="button"
                        class="btn btn-secondary"
                        @click="showCartModal = false"
                    >
                        Continue Shopping
                    </button>

                    <router-link :to="{name: 'checkout.index'}" class="btn btn-primary" @click="showCartModal = false">
                        Checkout
                    </router-link>
                    
                </div>
            </div>
        </div>
    </div>
</template>
