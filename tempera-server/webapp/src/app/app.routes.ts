import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_guards/is-logged-in.guard';
import { isNotLoggedInGuard } from './_guards/is-not-logged-in.guard';
import { UsersComponent } from './userManagement/users/users.component';
import { UserDetailsComponent } from './userManagement/user-details/user-details.component';
import { ValidationComponent } from './validation/validation.component';
import {RoomsComponent} from "./roomManagement/rooms/rooms.component";
import {FloorMapComponent} from "./rooms/floor-map/floor-map.component";
import {FloorPlanComponent} from "./rooms/floor-plan/floor-plan.component";

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      {
        path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: HomeComponent },
          { path: 'users', component: UsersComponent },
          { path: 'user/:id', component: UserDetailsComponent },
          { path: 'rooms', component: RoomsComponent},
          { path: 'map', component: FloorMapComponent},
          { path: 'plan', component: FloorPlanComponent },
        ],
      },
    ],
  },
  { path: 'login', canActivate: [isNotLoggedInGuard], component: LoginComponent },
  { path: 'validate', canActivate: [isNotLoggedInGuard], component: ValidationComponent },
  { path: '**', redirectTo: '' },
];
