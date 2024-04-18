import { Routes } from '@angular/router';
import {RegisterComponent} from "./register/register.component";
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardModeratorComponent } from './board-moderator/board-moderator.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { HomeComponent } from './home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_guards/is-logged-in.guard';
import { isNotLoggedInGuard } from './_guards/is-not-logged-in.guard';
import {UsersComponent} from "./userManagement/users/users.component";
import {UserDetailsComponent} from "./userManagement/user-details/user-details.component";
import {UserEditComponent} from "./userManagement/user-edit/user-edit.component";

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      { path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: HomeComponent },
        ] },
    ]
  },
  { path: 'login', canActivate: [isNotLoggedInGuard] , component: LoginComponent },
  { path: 'register', canActivate: [isNotLoggedInGuard] , component: RegisterComponent },

  // todo: create notFoundComponent
  // { path: '**', redirectTo: '/notfound' }

  // todo: insert other components
  // { path: 'profile', component: ProfileComponent },
  // { path: 'user', component: BoardUserComponent },
  // { path: 'mod', component: BoardModeratorComponent },
  // { path: 'admin', component: BoardAdminComponent },
  // { path: '', redirectTo: 'home', pathMatch: 'full' },
  // { path: 'users', component: UsersComponent }
];
