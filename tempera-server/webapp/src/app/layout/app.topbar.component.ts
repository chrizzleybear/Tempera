import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { AsyncPipe, DatePipe, NgClass, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { TooltipModule } from 'primeng/tooltip';
import { BadgeModule } from 'primeng/badge';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { AlertStoreService } from '../_stores/alert-store.service';
import { TagModule } from 'primeng/tag';
import { AlertDto } from '../../api';
import SeverityEnum = AlertDto.SeverityEnum;
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  standalone: true,
  imports: [RouterLink, NgClass, TooltipModule, BadgeModule, OverlayPanelModule, TableModule, ButtonModule, AsyncPipe, NgIf, DatePipe, TagModule, WrapFnPipe],
})
export class AppTopBarComponent {

  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(public layoutService: LayoutService, private authService: AuthService, private storageService: StorageService, public warningStoreService: AlertStoreService) {
    setInterval(() => {
      this.warningStoreService.refreshAlerts();
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

  removeAlert(warningEntry: string) {
    this.warningStoreService.removeAlert(warningEntry);
  }

  showAlertText(severity: SeverityEnum) {
    switch (severity) {
      case SeverityEnum.Info:
        return 'Info';
      case SeverityEnum.Warning:
        return 'Warning';
      default:
        return 'Unknown';
    }
  }

  getAlertSeverity(severity: SeverityEnum) {
    switch (severity) {
      case SeverityEnum.Info:
        return 'warning';
      case SeverityEnum.Warning:
        return 'danger';
      default:
        return 'primary';
    }
  }
}
