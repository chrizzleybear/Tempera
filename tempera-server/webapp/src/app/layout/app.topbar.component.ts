import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { AsyncPipe, DatePipe, NgClass, NgIf } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../_services/auth.service';
import { StorageService } from '../_services/storage.service';
import { TooltipModule } from 'primeng/tooltip';
import { BadgeModule } from 'primeng/badge';
import { OverlayPanel, OverlayPanelModule } from 'primeng/overlaypanel';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { AlertStoreService } from '../_stores/alert-store.service';
import { TagModule } from 'primeng/tag';
import { AlertDto } from '../../api';
import SeverityEnum = AlertDto.SeverityEnum;
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { ToastModule } from 'primeng/toast';
import { ColorSchemeService } from '../_services/color-scheme.service';
import { map, startWith } from 'rxjs';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  standalone: true,
  imports: [RouterLink, NgClass, TooltipModule, BadgeModule, OverlayPanelModule, TableModule, ButtonModule, AsyncPipe, NgIf, DatePipe, TagModule, WrapFnPipe, ToastModule],
})
export class AppTopBarComponent implements OnInit {

  items!: MenuItem[];

  @ViewChild('menubutton') menuButton!: ElementRef;

  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

  @ViewChild('topbarmenu') menu!: ElementRef;

  @ViewChild('alertsPanel') alertsPanel!: OverlayPanel;

  // For some reason, the initial value is not emitted after login
  initialColorSchemeClass = this.layoutService.config.colorScheme === 'light' ? 'pi pi-sun' : 'pi pi-moon';

  colorSchemeClass$ = this.layoutService.configUpdate$.pipe(
    map(x => {
      if (x.colorScheme === 'light') {
        return 'pi pi-sun';
      } else {
        return 'pi pi-moon';
      }
    }),
    startWith(this.initialColorSchemeClass));

  constructor(public layoutService: LayoutService, private authService: AuthService, private storageService: StorageService, public alertStoreService: AlertStoreService, private colorSchemeService: ColorSchemeService) {
  }

  ngOnInit(): void {
    this.alertStoreService.startAlertTimer();
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

  removeAlert(warningEntry: string, remainingAlerts: number) {
    if (remainingAlerts < 1) {
      this.alertsPanel.hide();
      // workaround to prevent flickering of the overlay panel
      setTimeout(() => {
        this.alertStoreService.removeAlert(warningEntry);
      }, 100);
    } else
    {
      this.alertStoreService.removeAlert(warningEntry);
    }
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

  toggleColorScheme() {
    this.colorSchemeService.toggleScheme();
  }
}
