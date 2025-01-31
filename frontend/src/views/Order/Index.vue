<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { formatPrice, orderApiEndpoint } from '@/assets/js/helpers'
import { useFetch } from '@/composables/useFetch'
import { useSwal } from "@/composables/useSwal"
import Pagination from '@/components/Pagination.vue'

const router = useRouter()
const { fetchJson } = useFetch()
const { errorAlert } = useSwal()

const orders = ref([])
const loading = ref(false)
const currentPage = ref(1)
const totalPages = ref(1)
const perPage = ref(10)
const sortBy = ref('id')
const sortOrder = ref('DESC')

async function fetchOrders() {
   loading.value = true
   
   try {
       const endpoint = orderApiEndpoint('') + 
           `?page=${currentPage.value}` +
           `&perPage=${perPage.value}` +
           `&sortBy=${sortBy.value}` +
           `&sortOrder=${sortOrder.value}`
       
       const response = await fetchJson(endpoint)
       orders.value = response.data || []
       totalPages.value = response.meta?.lastPage || 1
   } catch (err) {
       errorAlert("Failed to load orders")
       console.error(err)
   } finally {
       loading.value = false
   }
}

function handlePageChange(page) {
    currentPage.value = page
    window.scrollTo({ top: 0, behavior: 'smooth' })
    fetchOrders()
}

function handleSortChange(sort) {
    sortBy.value = sort
    sortOrder.value = sortOrder.value === 'ASC' ? 'DESC' : 'ASC'
    fetchOrders()
}

function handleViewOrder(orderId) {
    router.push({name: 'order.show', params: {id: orderId}})
}

onMounted(() => {
   fetchOrders()
})
</script>

<template>
   <div class="container py-4 min-vh-100">
       <div class="row justify-content-center">
           <div class="col-12 col-lg-10">
               <div class="d-flex align-items-center justify-content-between mb-3">

                    <div class="d-flex align-items-center mb-4">
                        <button class="btn btn-link text-dark p-0 me-3" @click="router.back()">
                            <i class="bi bi-arrow-left fs-4"></i>
                        </button>
                        <h1 class="h3 mb-0">My Orders</h1>
                    </div>
                
                   <div class="dropdown">
                       <button 
                           class="btn btn-white bg-white border shadow-sm dropdown-toggle" 
                           type="button" 
                           data-bs-toggle="dropdown"
                       >
                           <i class="bi me-2" :class="sortOrder === 'ASC' ? 'bi-sort-up' : 'bi-sort-down'"></i>
                           Sort by
                       </button>
                       <ul class="dropdown-menu">
                           <li>
                               <button 
                                   class="dropdown-item d-flex align-items-center justify-content-between"
                                   @click="handleSortChange('id')"
                               >
                                   <span>Order ID</span>
                                   <i 
                                        v-if="sortBy === 'id'"
                                        class="bi" 
                                        :class="sortOrder === 'ASC' ? 'bi-arrow-up' : 'bi-arrow-down'"
                                   ></i>
                               </button>
                               
                               <button 
                                   class="dropdown-item d-flex align-items-center justify-content-between"
                                   @click="handleSortChange('total')"
                               >
                                   <span>Total Amount</span>
                                   <i 
                                        v-if="sortBy === 'total'"
                                        class="bi" 
                                        :class="sortOrder === 'ASC' ? 'bi-arrow-up' : 'bi-arrow-down'"
                                   ></i>
                               </button>
                           </li>
                       </ul>
                   </div>
               </div>

               <div v-if="loading" class="d-flex justify-content-center py-5">
                   <div class="spinner-border">
                       <span class="visually-hidden">Loading...</span>
                   </div>
               </div>

               <template v-else>
                   <div class="row g-4">
                       <div class="col-12" v-for="order in orders" :key="order.id">
                           <div class="card bg-white border-0 shadow-sm rounded-4">
                               <div class="card-body p-4">
                                   <div class="row align-items-center">
                                       <div class="col-12 col-lg-4">
                                           <div class="d-flex align-items-center">
                                               <div class="bg-light rounded-3 p-3 me-3">
                                                   <i class="bi bi-box fs-4"></i>
                                               </div>
                                               <div>
                                                   <h6 class="mb-1">Order #{{ order.id }}</h6>
                                                   <div class="text-muted small">
                                                       {{ new Date(order.completedAt).toLocaleDateString() }}
                                                   </div>
                                               </div>
                                           </div>
                                       </div>

                                       <div class="col-12 col-lg-3 my-3 my-lg-0">
                                           <span class="badge bg-success bg-opacity-10 text-success px-3 py-2 rounded-3">
                                               {{ order.status }}
                                           </span>
                                       </div>

                                       <div class="col-12 col-lg-3">
                                           <div class="text-muted small mb-1">Total Amount</div>
                                           <div class="fw-bold">{{ formatPrice(order.total) }}</div>
                                       </div>

                                       <div class="col-12 col-lg-2">
                                           <button 
                                               class="btn btn-dark w-100" 
                                               @click="handleViewOrder(order.id)"
                                           >
                                               View Order
                                           </button>
                                       </div>
                                   </div>
                               </div>
                           </div>
                       </div>
                   </div>

                   <div v-if="orders.length === 0" class="text-center py-5">
                       <div class="bg-white rounded-4 shadow-sm p-5 mx-auto">
                           <i class="bi bi-inbox display-1 text-secondary mb-4 d-block"></i>
                           <h4 class="mb-3">No Orders Found</h4>
                           <p class="text-muted mb-4">You haven't placed any orders yet</p>
                           <button 
                               class="btn btn-dark px-4"
                               @click="router.push({name: 'home'})"
                           >
                               Start Shopping
                           </button>
                       </div>
                   </div>

                   <div class="d-flex justify-content-end mt-4" v-if="orders.length > 0">
                       <Pagination 
                           :current-page="currentPage"
                           :total-pages="totalPages"
                           @page-change="handlePageChange"
                       />
                   </div>
               </template>
           </div>
       </div>
   </div>
</template>