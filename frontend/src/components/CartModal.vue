<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { formatPrice } from '@/assets/js/helpers'
import { useCart } from '@/composables/useCart'

const { 
    cartItems, 
    showCartModal,
    updateCartItemQuantity, 
    removeFromCart 
} = useCart()

const modal = ref(null)
let bsModal = null

const total = computed(() => {
    return cartItems.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity)
    }, 0)
})

function handleQuantityUpdate(item, newQuantity) {
    if (newQuantity < 1 || newQuantity > item.stock) return
    updateCartItemQuantity(item.id, newQuantity)
}

function handleRemoveItem(itemId) {
    removeFromCart(itemId)
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
    } catch (e) {}
})

onUnmounted(() => {
    try {
        modal.value.removeEventListener('hidden.bs.modal')
    } catch (e) {}
})
</script>

<template>
    <div class="modal fade" tabindex="-1" ref="modal">
        <div class="modal-dialog modal-dialog-centered max-w-500px">
            <div class="modal-content bg-white">
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
                    <!-- Empty Cart State -->
                    <div v-if="cartItems.length === 0" class="text-center p-4">
                        <i class="bi bi-cart-x display-4"></i>
                        <p class="mt-3 mb-0">Your cart is empty</p>
                    </div>

                    <div class="p-3">
                        <div class="list-group">
                            <div v-for="item in cartItems"
                                 :key="item.id"
                                 class="list-group-item bg-body p-3"
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
                            <h5 class="mb-0 fw-semibold">
                                Total: <span class="fw-bold">{{ formatPrice(total) }}</span>
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
                    <button
                        type="button"
                        class="btn btn-primary"
                        @click="handleCheckout"
                    >
                        Checkout
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
