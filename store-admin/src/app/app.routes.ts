import { Routes } from '@angular/router';
import { DashboardSimpleComponent } from './pages/dashboard/dashboard-simple.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardSimpleComponent }
]; 