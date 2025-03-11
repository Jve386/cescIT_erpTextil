import { createRouter, createWebHistory } from 'vue-router';
import AgregarStock from '../pages/AgregarStockPage.vue';
import Ventas from '../pages/VentasPage.vue';
import ConsultarStock from '../pages/ConsultarStockPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/agregarstock',  // Redirige a /agregarstock
  },
  {
    path: '/agregarstock',  // Ruta de Agregar Stock
    name: 'AgregarStock',
    component: AgregarStock, // Página para agregar stock
  },
  {
    path: '/ventas',  // Ruta de Ventas
    name: 'Ventas',
    component: Ventas,
  },
  {
    path: '/consultar-stock',  // Ruta para consultar stock
    name: 'ConsultarStock',
    component: ConsultarStock,
  },
  {
    path: '/:catchAll(.*)*',  // Ruta de error 404
    component: () => import('pages/ErrorNotFound.vue'),
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
