import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_guards/is-logged-in.guard';
import { isNotLoggedInGuard } from './_guards/is-not-logged-in.guard';
import { UsersComponent } from './userManagement/users/users.component';
import { UserDetailsComponent } from './userManagement/user-details/user-details.component';
import { ValidationComponent } from './validation/validation.component';
import {ProjectsComponent} from "./projectManagement/projects/projects.component";
import {ProjectDetailsComponent} from "./projectManagement/project-details/project-details.component";
import {GroupsComponent} from "./groupManagement/groups/groups.component";
import {GroupDetailsComponent} from "./groupManagement/group-details/group-details.component";
import {GroupMembersComponent} from "./groupManagement/group-members/group-members.component";
import {ProjectGroupsComponent} from "./projectManagement/project-groups/project-groups.component";
import {isGroupLeadGuard} from "./_guards/is-groupLead.guard";
import {GroupsGroupleadComponent} from "./grouplead/groups-grouplead.component";
import {ProfileComponent} from "./profile/profile.component";

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      {
        path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: HomeComponent },
          { path: 'users', component: UsersComponent },
          { path: 'user/:id', component: UserDetailsComponent },
          { path: 'groups', component: GroupsComponent },
          { path: 'group/:id', component: GroupDetailsComponent},
          { path: 'group/members/:id', component: GroupMembersComponent},
          { path: 'project/groups/:id', component: ProjectGroupsComponent},
          { path: 'projects', component: ProjectsComponent},
          { path: 'project/:id', component: ProjectDetailsComponent},
          { path: 'myGroups', component: GroupsGroupleadComponent},
        ],
      },
    ],
  },
  { path: 'login', canActivate: [isNotLoggedInGuard], component: LoginComponent },
  { path: 'validate', canActivate: [isNotLoggedInGuard], component: ValidationComponent },
  { path: '**', redirectTo: '' },
];
