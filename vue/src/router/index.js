import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from "@/views/Layout";
import Cookies from "js-cookie";
import createWebHashHistory from 'vue-router'

Vue.use(VueRouter)

const routes = [
    // Login
    {
        path: '/login', name: 'Login', component: () => import('@/views/login/Login.vue')
    },
    // 主页
    {
        path: '/', name: 'Layout', component: Layout, redirect: '/home', // 重定向
        children: [ // 子路由
            {
                path: 'home', name: 'Home', component: () => import('@/views/home/HomeView.vue')
            },
            // User
            {
                path: 'userList', name: 'UserList', component: () => import('@/views/user/UserList.vue')
            },
            {
                path: 'addUser', name: 'AddUser', component: () => import('@/views/user/AddUser.vue')
            },
            {
                path: 'editUser', name: 'EditUser', component: () => import('@/views/user/EditUser.vue')
            },

            // Admin
            {
                path: 'adminList', name: 'AdminList', component: () => import('@/views/admin/AdminList.vue')
            },
            {
                path: 'addAdmin', name: 'AddAdmin', component: () => import('@/views/admin/AddAdmin.vue')
            },
            {
                path: 'editAdmin', name: 'EditAdmin', component: () => import('@/views/admin/EditAdmin.vue')
            },

            // Category
            {
                path: 'categoryList', name: 'CategoryList', component: () => import('@/views/category/CategoryList.vue')
            },
            {
                path: 'addCategory', name: 'AddCategory', component: () => import('@/views/category/AddCategory.vue')
            },
            {
                path: 'editCategory', name: 'EditCategory', component: () => import('@/views/category/EditCategory.vue')
            },

            // Book
            {
                path: 'bookList', name: 'BookList', component: () => import('@/views/book/BookList.vue')
            },
            {
                path: 'addBook', name: 'AddBook', component: () => import('@/views/book/AddBook.vue')
            },
            {
                path: 'editBook', name: 'EditBook', component: () => import('@/views/book/EditBook.vue')
            },

            // Borrow
            {
                path: 'borrowList', name: 'BorrowList', component: () => import('@/views/borrow/BorrowList.vue')
            },
            {
                path: 'addBorrow', name: 'AddBorrow', component: () => import('@/views/borrow/AddBorrow.vue')
            },
            {
                path: 'editBorrow', name: 'EditBorrow', component: () => import('@/views/borrow/EditBorrow.vue')
            }
        ]
    },
    {
        path: "*",
        component: () => import('@/views/404.vue')
    }
]

const router = new VueRouter({
    // mode: 'history',
    base: process.env.BASE_URL,
    //配置后刷新路由不会出现页面404的问题
    history: createWebHashHistory,
    routes,
})

// 守护路由
router.beforeEach((to, from, next) => {

    const admin_token = Cookies.get('Admin-Token')
    // 如果访问路径是登录页面则直接放行
    if (to.path === '/login') {
        next()
    }
    // 如果不是就获取Cookie中的Admin-Token
    // 如果token不存在且访问的路径也不是登录页
    if (!admin_token && to.path !== '/login') {
        // 跳转到登录页
        return next('/login')
    }



    // token存在且访问路径不为登录页就直接放行
    next()
});

export default router

