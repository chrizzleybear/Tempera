import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { AsyncPipe, NgClass, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { TooltipModule } from 'primeng/tooltip';
import { BadgeModule } from 'primeng/badge';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { WarningStoreService } from '../_stores/warning.store.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  standalone: true,
  imports: [RouterLink, NgClass, TooltipModule, BadgeModule, OverlayPanelModule, TableModule, ButtonModule, AsyncPipe, NgIf],
})
export class AppTopBarComponent {

  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(public layoutService: LayoutService, private authService: AuthService, private storageService: StorageService, public warningStoreService: WarningStoreService) {
    setInterval(() => {
      this.warningStoreService.refreshWarnings();
    }, 20 * 1000);
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

  removeWarning(warningEntry: string) {
    this.warningStoreService.removeWarning(warningEntry);
  }
}
