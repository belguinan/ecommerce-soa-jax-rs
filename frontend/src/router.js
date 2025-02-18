import { createRouter, createWebHistory } from 'vue-router';
import {getLocalStorage} from "@/assets/js/helpers";

const routes = [
    {
        path: '/',
        component: () => import('@/layouts/CustomerLayout.vue'),
        children: [
            {
                path: '',
                name: 'home',
                component: () => import('@/views/Home/Index.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Home'
                }
            },

            {
                path: '/profile',
                name: 'profile.edit',
                component: () => import('@/views/Profile/Edit.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Edit Profile'
                }
            },

            {
                path: '/product/create',
                name: 'product.create',
                component: () => import('@/views/Product/Form.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Create product'
                }
            },
            {
                path: '/product/:id/edit',
                name: 'product.edit',
                component: () => import('@/views/Product/Form.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Edit product'
                }
            },
            {
                path: '/product/:id',
                name: 'product.show',
                component: () => import('@/views/Product/Show.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Show product'
                }
            },
        ]
    },

    {
        path: '/',
        component: () => import('@/layouts/CustomerLayout.vue'),
        children: [
            {
                path: '/checkout',
                name: 'checkout.index',
                component: () => import('@/views/Checkout/Index.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Checkout'
                }
            },
            
            {
                path: '/order/:id',
                name: 'order.show',
                component: () => import('@/views/Order/Show.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'View order'
                }
            },
            {
                path: '/order',
                name: 'order.index',
                component: () => import('@/views/Order/Index.vue'),
                meta: {
                    requiresAuth: true,
                    title: 'Orders'
                }
            },
        ]
    },

    {
        path: '/stats',
        component: () => import('@/layouts/AdminLayout.vue'),
        children: [
            {
                path: '/stats',
                name: 'stats.index',
                component: () => import('@/views/Stats/Index.vue'),
                meta: {
                    requiresSuperAdmin: true,
                    title: 'Stats'
                }
            },
        ]
    },

    {
        path: '/',
        component: () => import('@/layouts/GuestLayout.vue'),
        children: [
            {
                path: '/login',
                name: 'login',
                component: () => import('@/views/Auth/Login.vue'),
                meta: {
                    guest: true,
                    title: 'Login'
                }
            },
            {
                path: '/signup',
                name: 'signup',
                component: () => import('@/views/Auth/Signup.vue'),
                meta: {
                    guest: true,
                    title: 'Signup'
                }
            },
        ]
    },
    
    {
        path: '/:pathMatch(.*)*',
        name: 'not-found',
        component: () => import('@/views/Errors/404.vue'),
        meta: {
            title: '404 Not Found'
        }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {

    document.title = `${to.meta.title} - eCommerce SOA`;

    const token = getLocalStorage('token');
    const user = getLocalStorage('user');

    if (to.meta?.requiresSuperAdmin && (!token || !user?.isSuperAdmin)) {
        next({ name: 'login' });
        return;
    }

    if (to.meta?.requiresAuth && user?.isSuperAdmin && to.name !== 'stats.index') {
        next({ name: 'stats.index' });
    }
    
    if (to.meta.requiresAuth && (!token || !user)) {
        next({ name: 'login' });
        return;
    }

    next();
});

export default router;