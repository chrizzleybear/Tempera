import { Routes } from '@angular/router';
import {RegisterComponent} from "./register/register.component";
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardModeratorComponent } from './board-moderator/board-moderator.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { HomeComponent } from './home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_helpers/isLoggedInGuard';

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      { path: '', canActivate: [isLoggedInGuard], children: [
          { path: 'home', component: HomeComponent },
        ] },
    ]
  },
  { path: 'login', component: LoginComponent },

  // todo: create notFoundComponent
  // { path: '**', redirectTo: '/notfound' }

  // todo: insert other components
  // { path: 'home', component: HomeComponent },
  // { path: 'login', component: LoginComponent },
  // { path: 'register', component: RegisterComponent },
  // { path: 'profile', component: ProfileComponent },
  // { path: 'user', component: BoardUserComponent },
  // { path: 'mod', component: BoardModeratorComponent },
  // { path: 'admin', component: BoardAdminComponent },
  // { path: '', redirectTo: 'home', pathMatch: 'full' }
];
