import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import Test1 from '../views/Test1.vue'
import Test2 from '../views/Test2.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        // 子路由, router跳转只会在Home里面去跳转
        children: [
            {
                path: '/test1',
                name: 'Test1',
                component: Test1
            },
            {
                path: '/test2',
                name: 'Test2',
                component: Test2
            }
        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
