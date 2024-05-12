import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_guards/is-logged-in.guard';
import { isNotLoggedInGuard } from './_guards/is-not-logged-in.guard';
import { UsersComponent } from './userManagement/users/users.component';
import { UserDetailsComponent } from './userManagement/user-details/user-details.component';
import { ValidationComponent } from './validation/validation.component';

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      {
        path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: DashboardComponent },
          { path: 'users', component: UsersComponent },
          { path: 'user/:id', component: UserDetailsComponent },
        ],
      },
    ],
  },
  { path: 'login', canActivate: [isNotLoggedInGuard], component: LoginComponent },
  { path: 'validate', canActivate: [isNotLoggedInGuard], component: ValidationComponent },
  { path: '**', redirectTo: '' },
];
