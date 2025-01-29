import { ref } from 'vue'
import { useFetch } from './useFetch'
import { statsApiEndpoint } from '@/assets/js/helpers'

export function useStats() {
    const { fetchJson } = useFetch()
    
    const loading = ref(true)
    const interval = ref('week')
    const dashboardStats = ref(null)
    const salesStats = ref(null)
    const productsStats = ref(null)
    const usersStats = ref(null)

    async function fetchStats() {
        loading.value = true
        try {
            const [dashboard, sales, products, users] = await Promise.all([
                fetchJson(statsApiEndpoint(`/dashboard?interval=${interval.value}`)),
                fetchJson(statsApiEndpoint(`/sales?interval=${interval.value}`)),
                fetchJson(statsApiEndpoint(`/products?interval=${interval.value}`)),
                fetchJson(statsApiEndpoint(`/users?interval=${interval.value}`))
            ])
            
            dashboardStats.value = dashboard
            salesStats.value = sales
            productsStats.value = products
            usersStats.value = users
        } catch (error) {
            console.error('Failed to fetch stats:', error)
            throw error
        } finally {
            loading.value = false
        }
    }

    return {
        loading,
        interval,
        dashboardStats,
        salesStats,
        productsStats,
        usersStats,
        fetchStats
    }
}