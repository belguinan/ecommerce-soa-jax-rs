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

    if (to.meta.requiresAuth && (!token || !user)) {
        next({ name: 'login' });
        return;
    }

    next();
});

export default router;