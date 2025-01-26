// vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// Import required PostCSS plugins
import autoprefixer from 'autoprefixer'
import purgecss from '@fullhuman/postcss-purgecss'

export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    css: {
        // postcss: {
        //     plugins: [
        //         autoprefixer(),
        //         purgecss({
        //             content: [
        //                 './index.html',
        //                 './src/**/*.{vue,js,ts,jsx,tsx}',
        //             ],
        //             safelist: {
        //                 standard: [
        //                     /^router-/,
        //                     /^vue-/,
        //                     /^v-/,
        //                     'html',
        //                     'body',
        //                 ],
        //                 deep: [/dropdown-menu$/],
        //                 greedy: [/^modal-/],
        //             },
        //             defaultExtractor: (content) => {
        //                 const contentWithoutStyleBlocks = content.replace(/<style[^]+?<\/style>/gi, '')
        //                 return contentWithoutStyleBlocks.match(/[A-Za-z0-9-_/:]*[A-Za-z0-9-_/]+/g) || []
        //             },
        //         })
        //     ]
        // },
        // preprocessorOptions: {
        //     scss: {
        //         additionalData: `@import "@/assets/sass/_variables.scss";`
        //     }
        // }
    },
    build: {
        assetsInlineLimit: 0,
        cssMinify: true,
        cssCodeSplit: true,
        // rollupOptions: {
        //     output: {
        //         manualChunks: {
        //             'images': ['./src/assets/images/']
        //         }
        //     }
        // },
    }
})