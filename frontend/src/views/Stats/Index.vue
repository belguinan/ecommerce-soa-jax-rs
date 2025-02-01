<script setup>
import { onMounted, computed } from 'vue'
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend } from 'chart.js'
import { Line, Bar } from 'vue-chartjs'
import { useStats } from '@/composables/useStats'
import { useSwal } from "@/composables/useSwal"
import { formatPrice } from "@/assets/js/helpers"

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, BarElement, Title, Tooltip, Legend)

const { errorAlert } = useSwal()
const {
    interval,
    dashboardStats,
    salesStats,
    productsStats,
    usersStats,
    fetchStats
} = useStats()

async function handleIntervalChange(newInterval) {
    interval.value = newInterval
    try {
        await fetchStats()
    } catch {
        errorAlert('Error loading statistics')
    }
}

const chartData = computed(() => ({
    labels: salesStats.value?.map(item =>
        new Date(item.createdAt).toLocaleDateString()
    ) || [],
    datasets: [
        {
            label: 'Sales',
            borderColor: '#202020',
            backgroundColor: '#202020',
            data: salesStats.value?.map(item => item.totalSales) || [],
            tension: 0.3
        },
        {
            label: 'Orders',
            borderColor: '#a300c0',
            backgroundColor: '#a300c0',
            data: salesStats.value?.map(item => item.totalOrders) || [],
            tension: 0.3
        },
        {
            label: 'Products Qty',
            borderColor: '#fac10a',
            backgroundColor: '#fac10a',
            data: salesStats.value?.map(item => item.totalProducts) || [],
            tension: 0.3
        }
    ]
}))

const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
            position: 'top'
        }
    },
    scales: {
        y: {
            beginAtZero: true
        }
    }
}

onMounted(async () => {
    try {
        await fetchStats()
    } catch {
        errorAlert('Error loading statistics')
    }
})
</script>

<template>
    <div class="container py-4 min-vh-100">
        <div class="row justify-content-center">
            <div class="col-12">
                <div class="d-flex align-items-center justify-content-between mb-4">
                    <div class="d-flex align-items-center">
                        <button class="btn btn-link text-dark p-0 me-3" @click="$router.back()">
                            <i class="bi bi-arrow-left fs-4"></i>
                        </button>
                        <h1 class="h3 mb-0">Dashboard</h1>
                    </div>

                    <div class="btn-group">
                        <button
                            v-for="option in ['day', 'week', 'month']"
                            :key="option"
                            @click="handleIntervalChange(option)"
                            class="btn"
                            :class="interval === option ? 'btn-dark' : 'btn-outline-dark'"
                        >
                            {{
                                option === 'day' ? "Today" :
                                option === 'week' ? 'Week' : 'Month'
                            }}
                        </button>
                    </div>
                </div>

                <div class="row g-4 mb-4">
                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden h-100 bg-white d-flex flex-row flex-nowrap">
                            <div class="bg-opacity-10 p-3 w-50 h-100 d-flex align-center justify-content-center" style="background-color: rgb(246 241 255);">
                                <i class="bi bi-cart text-primary my-auto fs-3"></i>
                            </div>
                            <div class="card-body p-4 d-flex align-items-center w-100 h-100">
                                <div class="d-flex flex-column mx-auto">
                                    <h6 class="card-title mb-2">Orders</h6>
                                    <h3 class="mb-0 text-center">{{ dashboardStats?.totalOrders || 0 }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden h-100 bg-white d-flex flex-row flex-nowrap">
                            <div class="bg-success bg-opacity-10 p-3 w-50 h-100 d-flex align-center justify-content-center">
                                <i class="bi bi-currency-dollar text-primary my-auto fs-3"></i>
                            </div>
                            <div class="card-body p-4 d-flex align-items-center w-100 h-100">
                                <div class="d-flex flex-column mx-auto">
                                    <h6 class="card-title mb-2">Total Sales</h6>
                                    <h3 class="mb-0 text-center">{{ formatPrice(dashboardStats?.totalSales || 0) }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-md-6 col-lg-4">
                        <div class="card border-0 shadow-sm rounded-4 overflow-hidden h-100 bg-white d-flex flex-row flex-nowrap">
                            <div class="bg-warning bg-opacity-10 p-3 w-50 h-100 d-flex align-center justify-content-center">
                                <i class="bi bi-box text-warning my-auto fs-3"></i>
                            </div>
                            <div class="card-body p-4 d-flex align-items-center w-100 h-100">
                                <div class="d-flex flex-column mx-auto">
                                    <h6 class="card-title mb-2">Products Sold</h6>
                                    <h3 class="mb-0 text-center">{{ dashboardStats?.totalProducts || 0 }}</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card border-0 shadow-sm rounded-4 mb-4 bg-white">
                    <div class="card-body p-4">
                        <h5 class="card-title mb-4">Sales Overview</h5>
                        <div style="height: 400px">
                            <component
                                :is="interval === 'day' ? Bar : Line"
                                :data="chartData"
                                :options="chartOptions"
                            />
                        </div>
                    </div>
                </div>

                <div class="row g-4">
                    <div class="col-12 col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 h-100 bg-white">
                            <div class="card-body p-4">
                                <h5 class="card-title mb-4">Top Products</h5>
                                <div class="table-responsive">
                                    <table class="table table-hover table-borderless align-middle">
                                        <thead>
                                            <tr>
                                                <th>Product</th>
                                                <th class="text-end">Sold</th>
                                                <th class="text-end">Revenue</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="product in productsStats" :key="product.productId">
                                                <td>
                                                    <div class="fw-semibold">{{ product.productName }}</div>
                                                </td>
                                                <td class="text-end">{{ product.totalQuantity }}</td>
                                                <td class="text-end">{{ formatPrice(product.totalSales) }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-12 col-lg-6">
                        <div class="card border-0 shadow-sm rounded-4 h-100 bg-white">
                            <div class="card-body p-4">
                                <h5 class="card-title mb-4">Top Customers</h5>
                                <div class="table-responsive">
                                    <table class="table table-hover table-borderless align-middle">
                                        <thead>
                                            <tr>
                                                <th>Customer</th>
                                                <th class="text-end">Orders</th>
                                                <th class="text-end">Spent</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr v-for="user in usersStats" :key="user.userId">
                                                <td>
                                                    <div class="fw-semibold">{{ user.userName }}</div>
                                                </td>
                                                <td class="text-end">{{ user.totalOrders }}</td>
                                                <td class="text-end">{{ formatPrice(user.totalSales) }}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>