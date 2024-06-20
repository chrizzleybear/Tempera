import { OnInit, Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { AppMenuitemComponent } from './app.menuitem.component';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { UserxDto } from '../../api';
import RolesEnum = UserxDto.RolesEnum;

@Component({
  selector: 'app-menu',
  templateUrl: './app.menu.component.html',
  standalone: true,
  imports: [NgFor, NgIf, AppMenuitemComponent],
})
export class AppMenuComponent implements OnInit {

  model: any[] = [];

  constructor(public layoutService: LayoutService, private authService: AuthService, private storageService: StorageService) {
  }

  ngOnInit() {
    // for examples of how to use the model, see the readme in this folder
    this.model = [
      {
        label: 'Home',
        items: [
          {
            label: 'Dashboard',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Employee),
          },
        ],
      },
      {
        label: 'My Account',
        items: [
          {
            label: 'Timetable',
            icon: 'pi pi-calendar-clock',
            routerLink: ['/timetable'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Employee),
          },
          {
            label: 'Climate Chart',
            icon: 'pi pi-fw pi-cloud',
            routerLink: ['/climateChart'],
          },
          {
            label: 'Climate Table',
            icon: 'pi pi-fw pi-table',
            routerLink: ['/climateTable']
          },
          {
            label: 'My Groups',
            icon: 'pi pi-fw pi-users',
            routerLink: ['/myGroups'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Grouplead),
          },
          ]},

      {
        label: 'Management',
        visible: this.storageService.getUser()?.roles.some(role => role === RolesEnum.Manager || role === RolesEnum.Grouplead || role === RolesEnum.Admin),
        items: [
          {
            label: 'Accumulated Time',
            icon: 'pi pi-fw pi-clock',
            routerLink: ['/accumulated-time'],
            visible: this.storageService.getUser()?.roles.some(role => role === RolesEnum.Manager || role === RolesEnum.Grouplead),
          },
          {
            label: 'User Management',
            icon: 'pi pi-fw pi-user',
            routerLink: ['/users'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Admin),
          },
          {
            label: 'Group Management',
            icon: 'pi pi-fw pi-users',
            routerLink: ['/groups'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Manager),
          },
          {
            label: 'Project Management',
            icon: 'pi pi-fw pi-clipboard',
            routerLink: ['/projects'],
            visible: this.storageService.getUser()?.roles.includes(RolesEnum.Manager),
          },
          {
            label: 'Room Management',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/rooms'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
        ],
      },
      {
        label: 'Tempera System',
        visible: this.storageService.getUser()?.roles.includes('ADMIN'),
        items: [
          {
            label: 'Access Points',
            icon: 'pi pi-fw pi-wifi',
            routerLink: ['/accessPoints'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
          {
            label: 'Tempera Stations',
            icon: 'pi pi-fw pi-gauge',
            routerLink: ['/temperaStations'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
          {
            label: 'Tips',
            icon: 'pi pi-fw pi-info-circle',
            routerLink: ['/tips'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
          {
            label: 'Audit Logs',
            icon: 'pi pi-fw pi-list',
            routerLink: ['/audit-logs'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
        ],
      },
    ];
  }

  logout() {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.storageService.clean();

        window.location.reload();
      },
      error: err => {
        console.log(err);
      },
    });
  }
}
