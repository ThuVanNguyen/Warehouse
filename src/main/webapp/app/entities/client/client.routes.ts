import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ClientResolve from './route/client-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const clientRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/client.component').then(m => m.ClientComponent),
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/client-detail.component').then(m => m.ClientDetailComponent),
    resolve: {
      client: ClientResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/client-update.component').then(m => m.ClientUpdateComponent),
    resolve: {
      client: ClientResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      hideButtons: false,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/client-update.component').then(m => m.ClientUpdateComponent),
    resolve: {
      client: ClientResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clientRoute;
