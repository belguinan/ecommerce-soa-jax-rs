import { ref, computed } from 'vue'
import { cartApiEndpoint } from '@/assets/js/helpers'
import { useFetch } from '@/composables/useFetch'

const cartItems = ref([])
const showCartModal = ref(false)
const { fetchJson } = useFetch()

export function useCart() {
    const cartItemCount = computed(() => {
        return cartItems.value.reduce((sum, item) => sum + item.quantity, 0)
    })

    async function loadCart() {
        try {
            const response = await fetchJson(cartApiEndpoint(''), {
                method: 'GET'
            })
            
            if (response.items) {
                cartItems.value = response.items.map(item => ({
                    id: item.id,
                    productId: item.product.id,
                    name: item.product.name,
                    price: item.price,
                    quantity: item.quantity,
                    stock: item.product.stock,
                    total: item.total
                }))
            }
        } catch (error) {
            console.error('Failed to load cart:', error)
            cartItems.value = []
        }
    }

    async function addToCart(product) {
        try {
            const response = await fetchJson(cartApiEndpoint(product.id), {
                method: 'POST',
                body: {
                    productId: product.id,
                    quantity: 1,
                    price: product.price
                }
            })
            
            if (response.items) {
                cartItems.value = response.items.map(item => ({
                    id: item.id,
                    productId: item.product.id,
                    name: item.product.name,
                    price: item.price,
                    quantity: item.quantity,
                    stock: item.product.stock,
                    total: item.total
                }))
            }

            if (!showCartModal.value) {
                showCartModal.value = true
            }

            return 'store'
        } catch (error) {
            console.error('Failed to add to cart:', error)
            throw error
        }
    }

    async function updateCartItemQuantity(itemId, quantity) {
        try {
            const response = await fetchJson(cartApiEndpoint(itemId), {
                method: 'PUT',
                body: { quantity }
            })
            
            if (response.items) {
                cartItems.value = response.items.map(item => ({
                    id: item.id,
                    productId: item.product.id,
                    name: item.product.name,
                    price: item.price,
                    quantity: item.quantity,
                    stock: item.product.stock,
                    total: item.total
                }))
            }
        } catch (error) {
            console.error('Failed to update cart:', error)
            throw error
        }
    }

    async function removeFromCart(itemId) {
        try {
            const response = await fetchJson(cartApiEndpoint(itemId), {
                method: 'DELETE'
            })

            if (response.items) {
                cartItems.value = response.items.map(item => ({
                    id: item.id,
                    productId: item.product.id,
                    name: item.product.name,
                    price: item.price,
                    quantity: item.quantity,
                    stock: item.product.stock,
                    total: item.total
                }))
            } else {
                cartItems.value = []
            }
        } catch (error) {
            console.error('Failed to remove from cart:', error)
            throw error
        }
    }

    function clearCart() {
        cartItems.value = []
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