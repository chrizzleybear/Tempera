import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AppLayoutComponent } from './layout/app.layout.component';
import { isLoggedInGuard } from './_guards/is-logged-in.guard';
import { isNotLoggedInGuard } from './_guards/is-not-logged-in.guard';
import { UsersComponent } from './userManagement/users/users.component';
import { UserDetailsComponent } from './userManagement/user-details/user-details.component';
import { ValidationComponent } from './validation/validation.component';
import { ProjectsComponent } from './projectManagement/projects/projects.component';
import { ProjectDetailsComponent } from './projectManagement/project-details/project-details.component';
import { GroupsComponent } from './groupManagement/groups/groups.component';
import { GroupDetailsComponent } from './groupManagement/group-details/group-details.component';
import { GroupMembersComponent } from './groupManagement/group-members/group-members.component';
import { ProjectGroupsComponent } from './projectManagement/project-groups/project-groups.component';
import { GroupsGroupleadComponent } from './grouplead/groups-grouplead.component';
import { GroupProjectsComponent } from './grouplead/group-projects/group-projects.component';
import { RoomsComponent } from './roomManagement/rooms/rooms.component';
import { FloorPlanComponent } from './rooms/floor-plan/floor-plan.component';
import { RoomDetailsComponent } from './roomManagement/room-details/room-details.component';
import { TimetableComponent } from './timetable/timetable.component';
import {AccesspointsComponent} from "./accessPointManagement/access-points/accespoints.component";
import {TemperaStationsComponent} from "./temperaManagement/tempera-stations/tempera-stations.component";
import {
  TemperaStationDetailsComponent
} from "./temperaManagement/tempera-station-details/tempera-station-details.component";
import {AccessPointDetailsComponent} from "./accessPointManagement/access-point-details/access-point-details.component";
import { AccumulatedTimeComponent } from './accumulated-time/accumulated-time.component';

export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      {
        path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: DashboardComponent },
          { path: 'users', component: UsersComponent },
          { path: 'user/:id', component: UserDetailsComponent },
          { path: 'rooms', component: RoomsComponent },
          { path: 'room/:id', component: RoomDetailsComponent },
          { path: 'plan', component: FloorPlanComponent },
          { path: 'groups', component: GroupsComponent },
          { path: 'group/:id', component: GroupDetailsComponent },
          { path: 'group/members/:name/:id', component: GroupMembersComponent },
          { path: 'project/groups/:id', component: ProjectGroupsComponent },
          { path: 'projects', component: ProjectsComponent },
          { path: 'project/:id', component: ProjectDetailsComponent },
          { path: 'myGroups', component: GroupsGroupleadComponent },
          { path: 'group/projects/:id', component: GroupProjectsComponent },
          { path: 'timetable', component: TimetableComponent },
          { path: 'accumulated-time', component: AccumulatedTimeComponent },
          { path: 'accessPoints', component: AccesspointsComponent},
          { path: 'accessPoint/:id', component: AccessPointDetailsComponent},
          { path: 'temperaStations', component: TemperaStationsComponent},
          { path: 'temperaStation/:id', component: TemperaStationDetailsComponent},
        ],
      },
    ],
  },
  { path: 'login', canActivate: [isNotLoggedInGuard], component: LoginComponent },
  { path: 'validate', canActivate: [isNotLoggedInGuard], component: ValidationComponent },
  { path: '**', redirectTo: '' },
];
