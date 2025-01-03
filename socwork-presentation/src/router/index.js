import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      alias:['/creer-compte', '/index.html'],
      name: 'home',
      component: () => import('../views/AcountCreateView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
    },
    // {
    //   path: '/index.html',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (About.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import('../views/AcountCreateView.vue'),
    // },
    {
      path: "/:notFound",
      name: 'error-404',
      component: () => import('../views/error404View.vue'),
    }
  ],
})

export default router
