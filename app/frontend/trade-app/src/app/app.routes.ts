import { Routes } from '@angular/router';
import {LoginComponent} from './pages/auth/login/login.component';
import {RegisterComponent} from './pages/auth/register/register.component';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // After login, load a portal that redirects to the proper area based on role
  { path: '', loadComponent: () => import('./pages/portal/portal.component').then(m => m.PortalComponent), canActivate: [authGuard] },

  // Manager area with navbar and child routes
  {
    path: 'manager',
    loadComponent: () => import('./pages/manager/shell/manager-shell.component').then(m => m.ManagerShellComponent),
    canActivate: [roleGuard],
    data: { roles: ['manager'] },
    children: [
      { path: '', loadComponent: () => import('./pages/trades/list-trades/trades.component').then(m => m.TradesComponent) },
      { path: 'users', loadComponent: () => import('./pages/users/user-list/user-list.component').then(m => m.UserListComponent) },
      { path: 'instruments', loadComponent: () => import('./pages/instruemnts/list-isntruments/manager-instruments.component').then(m => m.ManagerInstrumentsComponent) },
      { path: 'accounts', loadComponent: () => import('./pages/accounts/list-accounts/manager-accounts.component').then(m => m.ManagerAccountsComponent) },
    ]
  },

  // Broker area with navbar and child routes
  {
    path: 'broker',
    loadComponent: () => import('./pages/broker/shell/broker-shell.component').then(m => m.BrokerShellComponent),
    canActivate: [roleGuard],
    data: { roles: ['broker'] },
    children: [
      { path: '', loadComponent: () => import('./pages/trades/list-trades/trades.component').then(m => m.TradesComponent) },
      { path: 'accounts-status', loadComponent: () => import('./pages/account-status/list-account-status/broker-accounts-status.component').then(m => m.BrokerAccountsStatusComponent) },
    ]
  },

  { path: '**', redirectTo: '' },
];


