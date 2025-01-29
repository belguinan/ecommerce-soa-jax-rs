<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCart } from '@/composables/useCart'
import { useSwal } from "@/composables/useSwal"
import { useFetch } from "@/composables/useFetch"
import { formatPrice, getLocalStorage } from '@/assets/js/helpers'
import { orderApiEndpoint } from '../../assets/js/helpers'

const router = useRouter()
const { cartItems, total: cartTotal } = useCart()
const { errorAlert, successAlert } = useSwal()
const { fetchJson } = useFetch()

const user = getLocalStorage('user')

const form = ref({
    name: user?.name || '',
    address: user?.address || '',
    city: user?.city || '',
    phoneNumber: user?.phoneNumber || ''
})

const loading = ref(false)
const isCompleted = ref(false)
const errorResponse = ref({})

const subtotal = computed(() => {
    return cartItems.value.reduce((sum, item) => {
        return sum + (item.price * item.quantity)
    }, 0)
})

watch(() => form.value.phoneNumber, (newValue) => {
    if (newValue) {
        form.value.phoneNumber = formatPhoneNumber(newValue);
    }
});

const total = computed(() => {
    return subtotal.value
})

function validateForm() {
    let isValid = true
    errorResponse.value = {}

    if (!form.value.name) {
        errorResponse.value.name = 'Name is required'
        isValid = false
    } else if (form.value.name.length < 2) {
        errorResponse.value.name = 'Name must be at least 2 characters'
        isValid = false
    }

    if (!form.value.address) {
        errorResponse.value.address = 'Address is required'
        isValid = false
    } else if (form.value.address.length < 5) {
        errorResponse.value.address = 'Please enter a valid address'
        isValid = false
    }

    if (!form.value.city) {
        errorResponse.value.city = 'City is required'
        isValid = false
    }

    if (!form.value.phoneNumber) {
        errorResponse.value.phoneNumber = 'Phone number is required'
        isValid = false
    } else if (form.value.phoneNumber.replace(/\D/g, '').length !== 10) {
        errorResponse.value.phoneNumber = 'Please enter a valid 10-digit phone number'
        isValid = false
    }

    return isValid
}

async function handleConfirmOrder() {
    if (loading.value) return

    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
        errorResponse.value = { errors: validationErrors };
        return;
    }
    
    loading.value = true
    document.body.loader().show()
    
    try {
        const response = await fetchJson(orderApiEndpoint('/checkout'), {
            method: 'POST',
            body: form.value
        })

        document.body.loader().hide()
        await successAlert('Order placed successfully!')

        isCompleted.value = true;
        cartItems.value = []

        router.push({name: 'order.show', params: {id: response.id}})
        
    } catch (err) {
        errorAlert('Failed to place order')
        console.error(err)
    } finally {
        loading.value = false
        document.body.loader().hide()
    }
}

function handleCancel() {
    router.back()
}

watch(cartItems, newValue => {
    if (!isCompleted.value && newValue.length === 0) {
        router.push({ name: 'home' })
    }
})

onMounted(() => {
    setTimeout(() => {
        if (cartItems.value.length === 0) {
            router.push({ name: 'home' })
        }
    }, 500)
})
</script>

<template>
    <div class="container py-4 min-vh-100">
        <div class="row justify-content-center">
            <div class="col-12 col-lg-10">
                <div class="d-flex align-items-center mb-4">
                    <button class="btn btn-link text-dark p-0 me-3" @click="handleCancel">
                        <i class="bi bi-arrow-left fs-4"></i>
                    </button>
                    <h1 class="h3 mb-0">Checkout</h1>
                </div>

                <div class="row g-4">
                    <div class="col-lg-8">
                        <div class="card bg-white border-0 shadow-sm rounded-4">
                            <div class="card-body p-4">
                                <h5 class="mb-4">Order Summary</h5>
                                <div class="list-group rounded-4">
                                    <div v-for="item in cartItems" :key="item.id" class="list-group-item bg-body p-4">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <h6 class="mb-1 fw-semibold">{{ item.name }}</h6>
                                                <small class="text-muted">
                                                    {{ formatPrice(item.price) }} × {{ item.quantity }}
                                                </small>
                                            </div>
                                            <span class="fw-bold">
                                                {{ formatPrice(item.price * item.quantity) }}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-5">
                                    <h5 class="mb-4">Shipping Information</h5>
                                    <form @submit.prevent="handleConfirmOrder" class="row g-4">
                                        <div class="col-12">
                                            <div class="form-floating" :class="{'is-invalid': errorResponse.name}">
                                                <input type="text" class="form-control bg-light" :class="{'is-invalid': errorResponse.name}" v-model="form.name" id="name" placeholder="Full Name" required>
                                                <label for="name">Full Name</label>
                                                <div v-if="errorResponse.name" class="invalid-feedback">
                                                    {{ errorResponse.name }}
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-12">
                                            <div class="form-floating" :class="{'is-invalid': errorResponse.address}">
                                                <input type="text" class="form-control bg-light" :class="{'is-invalid': errorResponse.address}" v-model="form.address" id="address" placeholder="Delivery Address" required>
                                                <label for="address">Delivery Address</label>
                                                <div v-if="errorResponse.address" class="invalid-feedback">
                                                    {{ errorResponse.address }}
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-floating" :class="{'is-invalid': errorResponse.city}">
                                                <input type="text" class="form-control bg-light" :class="{'is-invalid': errorResponse.city}" v-model="form.city" id="city" placeholder="City" required>
                                                <label for="city">City</label>
                                                <div v-if="errorResponse.city" class="invalid-feedback">
                                                    {{ errorResponse.city }}
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-6">
                                            <div class="form-floating" :class="{'is-invalid': errorResponse.phoneNumber}">
                                                <input type="tel" class="form-control bg-light" :class="{'is-invalid': errorResponse.phoneNumber}" v-model="form.phoneNumber" id="phone" placeholder="Phone Number" required>
                                                <label for="phone">Phone Number</label>
                                                <div v-if="errorResponse.phoneNumber" class="invalid-feedback">
                                                    {{ errorResponse.phoneNumber }}
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-4">
                        <div class="card bg-white border-0 shadow-sm rounded-4 h-100 max-h-600px">
                            <div class="card-body d-flex flex-column p-4 h-100">
                                <div class="mt-0  mb-auto">
                                    <h5 class="mb-4">Order Total</h5>

                                    <div class="d-flex justify-content-between mb-3">
                                        <span class="text-muted">Subtotal</span>
                                        <span>{{ formatPrice(subtotal) }}</span>
                                    </div>

                                    <div class="d-flex justify-content-between mb-3">
                                        <span class="text-muted">Shipping</span>
                                        <span>{{ formatPrice(0.00) }}</span>
                                    </div>

                                    <hr class="my-4">

                                    <div class="d-flex justify-content-between mb-4">
                                        <span class="h5 mb-0">Total</span>
                                        <span class="h5 mb-0 fw-bold">{{ formatPrice(total || 0) }}</span>
                                    </div>
                                </div>

                                <div class="mt-auto d-grid gap-2">
                                    <button type="button" class="btn btn-dark py-3 fw-semibold" @click="handleConfirmOrder" :disabled="loading">
                                        <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
                                        Confirm Order
                                    </button>
                                    <button type="button" class="btn bg-light py-3" @click="handleCancel">
                                        Cancel
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>