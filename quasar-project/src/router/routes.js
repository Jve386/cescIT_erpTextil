const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      // Ruta por defecto de la página principal
      { path: '', component: () => import('pages/ArticulosPage.vue') },  // ARTICULOS
    ],
  },

  // Ruta de error 404
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
]

export default routes;
