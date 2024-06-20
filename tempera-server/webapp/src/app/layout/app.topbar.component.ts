import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { AsyncPipe, DatePipe, NgClass, NgForOf, NgIf } from '@angular/common';
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
import { AlertToEmojiPipe } from '../_pipes/alert-to-emoji.pipe';
import { SensorType } from '../models/threshold.model';
import { DialogModule } from 'primeng/dialog';

const tempHighHints = [
  'Ventilate: Open windows and doors in the cooler morning or evening hours to let in fresh air',
  'Use fans: Use fans to improve air circulation and provide a cooler atmosphere',
  'Darkening: Close curtains or blinds to reduce direct sunlight',
  'Use air conditioning: If possible, use air conditioning to effectively lower the room temperature.',
  'Reduce internal heat sources: Turn off or reduce the use of electronic devices to minimize internal heat emission in the room.',
];

const tempLowHints = [
  'Heating: Use radiators or the corresponding control panels for room climate control',
  'Close draughts: Check windows and doors and close them to reduce draughts',
  'Layering of clothing: In the event of a technical fault, several layers of warm clothing can provide temporary relief',
];

const humidityHighHints = [
  'Use of dehumidifiers: Use dehumidifiers to remove excess moisture from the air',
  'Ventilation: Ventilate the room regularly to remove moisture and improve air circulation',
  'Avoiding water sources: Reduce the use of water vapor generating appliances such as kettles or humidifiers',
];

const humidityLowHints = [
  'Use of humidifiers: Place humidifiers in the room to increase the humidity level',
  'Plants: Place indoor plants as they release moisture',
  'Avoid using air dryers/air conditioners: Avoid using air conditioners, as these can lower the humidity even further.',
];

const irradianceHighHints = [
  'Use of dimmers: If given, use dimmer switches to flexibly adjust the brightness of the lighting and reduce it when needed',
  'Use lampshades or diffusers: Place lampshades or diffusers over the light sources to diffuse the light and create softer lighting',
  'Reducing the number of light sources: Turn off some lamps or lights',
];

const irradianceLowHints = [
  'Use of additional light sources: Use additional light sources such as desk lamps or floor lamps',
  'Use of daylight lamps: Use daylight lamps to simulate natural daylight',
  'Optimize natural lighting: Open curtains or blinds to let in more natural light and position furniture so that it does not obscure light sources where possible',
];

const qualityHints = [
  'Ventilation: Ensure regular ventilation in the office to improve the air quality. Open windows to let in fresh air.',
  'Plants: Bring some air-purifying plants into the office. These can improve air quality. ',
  'Environmentally friendly cleaning products: Make sure that environmentally friendly cleaning products are used in the office.',
];

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  standalone: true,
  imports: [RouterLink, NgClass, TooltipModule, BadgeModule, OverlayPanelModule, TableModule, ButtonModule, AsyncPipe, NgIf, DatePipe, TagModule, WrapFnPipe, ToastModule, AlertToEmojiPipe, DialogModule, NgForOf],
})
export class AppTopBarComponent implements OnInit {

  items!: MenuItem[];

  tippDialogVisible: boolean = false;
  tipSensorType?: SensorType;
  protected highHints: string[] = [];
  protected lowHints: string[] = [];

  protected readonly SensorType = SensorType;
  protected readonly qualityHints = qualityHints;

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
    this.alertStoreService.showHint.subscribe(x => {
      switch (x) {
        case SensorType.TEMPERATURE:
          this.highHints = tempHighHints;
          this.lowHints = tempLowHints;
          break;
        case SensorType.HUMIDITY:
          this.highHints = humidityHighHints;
          this.lowHints = humidityLowHints;
          break;
        case SensorType.IRRADIANCE:
          this.highHints = irradianceHighHints;
          this.lowHints = irradianceLowHints;
          break;
      }
      this.alertsPanel.hide();
      this.tipSensorType = x;
      this.tippDialogVisible = true;
    });
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
    } else {
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
