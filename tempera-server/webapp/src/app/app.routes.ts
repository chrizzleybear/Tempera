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
import { GroupsComponent } from './groupManagement/groups/groups.component';
import { GroupDetailsComponent } from './groupManagement/group-details/group-details.component';
import { GroupMembersComponent } from './groupManagement/group-members/group-members.component';
import { ProjectGroupsComponent } from './projectManagement/project-groups/project-groups.component';
import { GroupsGroupleadComponent } from './grouplead/groups-grouplead.component';
import { GroupProjectsComponent } from './grouplead/group-projects/group-projects.component';
import { RoomsComponent } from './roomManagement/rooms/rooms.component';
import { RoomDetailsComponent } from './roomManagement/room-details/room-details.component';
import { TimetableComponent } from './timetable/timetable.component';
import {AccesspointsComponent} from "./accessPointManagement/access-points/accespoints.component";
import {TemperaStationsComponent} from "./temperaManagement/tempera-stations/tempera-stations.component";
import {
  TemperaStationDetailsComponent
} from "./temperaManagement/tempera-station-details/tempera-station-details.component";
import {AccessPointDetailsComponent} from "./accessPointManagement/access-point-details/access-point-details.component";
import { AccumulatedTimeComponent } from './accumulated-time/accumulated-time.component';
import {OverviewTablesComponent} from "./climateTables/overview-tables/overview-tables.component";
import {TipsComponent} from "./tips/tips.component";
import { AuditLogsComponent } from './audit-logs/audit-logs.component';
import {OverviewChartsComponent} from "./climateCharts/overview-charts.component";
import { hasAnyOfPermissionsGuard } from './_guards/has-any-of-permissions.guard';
import { UserxDto } from '../api';
import RolesEnum = UserxDto.RolesEnum;


export const routes: Routes = [
  {
    path: '', component: AppLayoutComponent, children: [
      {
        path: '', canActivate: [isLoggedInGuard], children: [
          { path: '', component: DashboardComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Employee])] },
          { path: 'users', component: UsersComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
          { path: 'user/:id', component: UserDetailsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
          { path: 'rooms', component: RoomsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
          { path: 'room/:id', component: RoomDetailsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
          { path: 'groups', component: GroupsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Grouplead])] },
          { path: 'group/:id', component: GroupDetailsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Grouplead])] },
          { path: 'group/members/:name/:id', component: GroupMembersComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Grouplead])] },
          { path: 'project/groups/:id', component: ProjectGroupsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Manager])] },
          { path: 'projects', component: ProjectsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Manager])] },
          { path: 'myGroups', component: GroupsGroupleadComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Grouplead])] },
          { path: 'group/projects/:name/:id', component: GroupProjectsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Grouplead])] },
          { path: 'timetable', component: TimetableComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Employee])] },
          { path: 'accumulated-time', component: AccumulatedTimeComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Manager, RolesEnum.Grouplead])] },
          { path: 'accessPoints', component: AccesspointsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])]},
          { path: 'accessPoint/:id', component: AccessPointDetailsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])]},
          { path: 'temperaStations', component: TemperaStationsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])]},
          { path: 'temperaStation/:id', component: TemperaStationDetailsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])]},
          { path: 'climateChart', component: OverviewChartsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])]},
          { path: 'climateTable', component: OverviewTablesComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Employee])]},
          { path: 'audit-logs', component: AuditLogsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
          { path: 'tips', component: TipsComponent, canActivate: [hasAnyOfPermissionsGuard([RolesEnum.Admin])] },
        ],
      },
    ],
  },
  { path: 'login', canActivate: [isNotLoggedInGuard], component: LoginComponent },
  { path: 'validate', canActivate: [isNotLoggedInGuard], component: ValidationComponent },
  { path: '**', redirectTo: '' },
];
