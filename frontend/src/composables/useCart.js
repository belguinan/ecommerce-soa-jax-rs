import { ref, computed } from 'vue'
import {useSwal} from "@/composables/useSwal";

const cartItems = ref([])
const showCartModal = ref(false)

export function useCart() {
    const cartItemCount = computed(() => {
        return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
    })

    function loadCart() {
        const savedCart = localStorage.getItem('cart')
        if (savedCart) {
            cartItems.value = JSON.parse(savedCart)
        }
    }

    async function addToCart(product) {
        const existingItem = cartItems.value.find(item => item.id === product.id)
        let action = '';

        if (existingItem && existingItem.quantity < product.stock) {
            existingItem.quantity++
            action = 'update'
        } else if (!existingItem) {
            action = 'store'
            cartItems.value.push({
                ...product,
                quantity: 1
            })

            if (! showCartModal.value) {
                showCartModal.value = true
            }
        }

        localStorage.setItem('cart', JSON.stringify(cartItems.value))

        return action;
    }

    function updateCartItemQuantity(productId, quantity) {
        const item = cartItems.value.find(item => item.id === productId)
        if (item) {
            item.quantity = quantity
            localStorage.setItem('cart', JSON.stringify(cartItems.value))
        }
    }

    function removeFromCart(productId) {
        cartItems.value = cartItems.value.filter(item => item.id !== productId)
        localStorage.setItem('cart', JSON.stringify(cartItems.value))
    }

    function clearCart() {
        cartItems.value = []
        localStorage.removeItem('cart')
    }

    return {
        cartItems,
        cartItemCount,
        showCartModal,
        loadCart,
        addToCart,
        updateCartItemQuantity,
        removeFromCart,
        clearCart
    }
}