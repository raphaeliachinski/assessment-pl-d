import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'calculate-profit', pathMatch: 'full' },
  {
    path: 'calculate-profit',
    loadComponent: () =>
      import('./features/profit-loss/profit-loss-page/profit-loss-page.component').then(
        (m) => m.ProfitLossPageComponent,
      ),
  },
  {
    path: 'shipments/:shipmentNumber/calculate-profit',
    loadComponent: () =>
      import('./features/profit-loss/profit-loss-page/profit-loss-page.component').then(
        (m) => m.ProfitLossPageComponent,
      ),
  },
  { path: '**', redirectTo: 'calculate-profit' },
];
