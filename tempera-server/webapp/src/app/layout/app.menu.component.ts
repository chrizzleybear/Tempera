import { OnInit, Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { AppMenuitemComponent } from './app.menuitem.component';
import { NgFor, NgIf } from '@angular/common';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';

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
          { label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] },
        ],
      },
      {
        label: 'UI Components',
        items: [
          { label: 'Logout', icon: 'pi pi-fw pi-id-card', command: () => this.logout() },
          {
            label: 'User-Management',
            icon: 'pi pi-fw pi-user',
            routerLink: ['/users'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN'),
          },
          {
            label: 'Group-Management',
            icon: 'pi pi-fw pi-users',
            routerLink: ['/groups'],
            visible: this.storageService.getUser()?.roles.includes('MANAGER'),
          },
          { label: 'Project-Management',
            icon: 'pi pi-fw pi-clipboard',
            routerLink: ['/projects'],
            visible: this.storageService.getUser()?.roles.includes('MANAGER') },
          { label: 'My Groups',
            icon: 'pi pi-fw pi-users',
            routerLink: ['/myGroups'],
            visible: this.storageService.getUser()?.roles.includes('GROUPLEAD')
          },
          { label: 'Room Management',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/rooms'],
            visible: this.storageService.getUser()?.roles.includes('ADMIN') },

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
