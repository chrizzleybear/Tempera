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
        this.model = [
            {
                label: 'Home',
                items: [
                    { label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] }
                ]
            },
            {
                label: 'UI Components',
                items: [
                  // todo: put into seperate function
                    { label: 'Logout', icon: 'pi pi-fw pi-id-card', command: () =>  this.authService.logout().subscribe({
                        next: res => {
                          console.log(res);
                          this.storageService.clean();

                          window.location.reload();
                        },
                        error: err => {
                          console.log(err);
                        }
                      })},
                    { label: 'User-Management', icon: 'pi pi-fw pi-check-square', routerLink: ['/users'], visible: this.storageService.getUser()?.roles.includes('ADMIN') },
                    { label: 'Project-Management', icon: 'pi pi-fw pi-clipboard', routerLink: ['/projects'], visible: this.storageService.getUser()?.roles.includes('MANAGER') },
                ]
            }
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
