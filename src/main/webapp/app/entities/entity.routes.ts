import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'wareHouseApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'wareHouseApp.client.home.title' },
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'order',
    data: { pageTitle: 'wareHouseApp.order.home.title' },
    loadChildren: () => import('./order/order.routes'),
  },
  {
    path: 'product-image',
    data: { pageTitle: 'wareHouseApp.productImage.home.title' },
    loadChildren: () => import('./product-image/product-image.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
