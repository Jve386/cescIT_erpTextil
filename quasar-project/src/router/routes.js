const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),  // Layout principal
    children: [
      // Ruta para Agregar Stock
      { path: 'agregarstock', component: () => import('src/pages/AgregarStockPage.vue') },

      // Ruta para Consultar Stock
      { path: 'consultarsock', component: () => import('src/pages/ConsultarStockPage.vue') },

      // Ruta para Ventas
      { path: 'ventas', component: () => import('pages/VentasPage.vue') },
    ],
  },

  // Ruta de error 404
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
]

export default routes;
