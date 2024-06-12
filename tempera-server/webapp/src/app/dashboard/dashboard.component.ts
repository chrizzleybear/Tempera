import { Component, DestroyRef, OnInit } from '@angular/core';
import { User } from '../models/user.model';
import { DatePipe, NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AirQualityPipe } from '../_pipes/air-quality.pipe';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import {
  DashboardControllerService,
  DashboardDataResponse,
  SimpleGroupxProjectDto, Threshold,
  UserxDto,
} from '../../api';
import VisibilityEnum = DashboardDataResponse.VisibilityEnum;
import { StorageService } from '../_services/storage.service';
import { ButtonModule } from 'primeng/button';
import RolesEnum = UserxDto.RolesEnum;
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MessagesModule } from 'primeng/messages';
import { Observable, switchMap, timer } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DisplayHelper } from '../_helpers/display-helper';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { OverlappingProjectHelper } from '../_helpers/overlapping-project-helper';
import { AlertStoreService } from '../_stores/alert-store.service';
import { TooltipModule } from 'primeng/tooltip';
import { MeasurementComponent } from './measurement/measurement.component';
import { SensorType } from '../models/threshold.model';

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
  ]

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgIf,
    MessageModule,
    DropdownModule,
    TableModule,
    TagModule,
    DatePipe,
    AirQualityPipe,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    ButtonModule,
    WrapFnPipe,
    ReactiveFormsModule,
    MessagesModule,
    ToastModule,
    TooltipModule,
    MeasurementComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  public dashboardData?: DashboardDataResponse;

  public user?: User;

  public colleagueTableFilterFields: string[] = [];

  public visibilityOptions: VisibilityEnum[] = Object.values(VisibilityEnum);

  /**
   * This observable handles fetching the dashboard data every minute
   */
  private getData$(username: string): Observable<DashboardDataResponse> {
    return timer(0, 1000 * 60).pipe(
      switchMap(() => {
        return this.dashboardControllerService.getDashboardData(username);
      }),
    );
  }

  protected readonly DisplayHelper = DisplayHelper;
  protected readonly Threshold = Threshold;
  protected readonly SensorType = SensorType;
  protected readonly tempHighHints = tempHighHints;
  protected readonly tempLowHints = tempLowHints;
  protected readonly humidityHighHints = humidityHighHints;
  protected readonly humidityLowHints = humidityLowHints;
  protected readonly irradianceHighHints = irradianceHighHints;
  protected readonly irradianceLowHints = irradianceLowHints;
  protected readonly qualityHints = qualityHints;

  public form = new FormGroup({
    visibility: new FormControl<VisibilityEnum>(VisibilityEnum.Public, { nonNullable: true }),
    project: new FormControl<SimpleGroupxProjectDto | undefined>(undefined, { nonNullable: true }),
  });

  /*
  Used for handling when a user is assigned to a project from multiple groups
  Key is the projectId and value is an object containing the projects with the same projectId and the original name of the project
   */
  private duplicatedProjects: Map<string, {
    projects: SimpleGroupxProjectDto[],
    originalName: string
  }> = new Map<string, {
    projects: SimpleGroupxProjectDto[],
    originalName: string
  }>();

  constructor(
    private dashboardControllerService: DashboardControllerService,
    private storageService: StorageService,
    private destroyRef: DestroyRef,
    private messageService: MessageService,
    private alertStoreService: AlertStoreService) {
  }

  ngOnInit(): void {
    this.user = this.storageService.getUser();

    if (this.user) {
      this.getData$(this.user.username).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: data => {
          this.alertStoreService.refreshAlerts();

          this.dashboardData = data;
          this.colleagueTableFilterFields = Object.keys(this.dashboardData?.colleagueStates?.[0] ?? []);

          this.duplicatedProjects = OverlappingProjectHelper.getDuplicatedProjects(this.dashboardData.availableProjects ?? []);
          OverlappingProjectHelper.renameOverlappingProjects(this.duplicatedProjects, this.dashboardData.availableProjects ?? []);

          // set form values in case of existing data
          this.form.controls.visibility.setValue(this.dashboardData.visibility);
          if (this.user?.roles?.includes(RolesEnum.Admin)) {
            this.form.controls.visibility.disable();
          }
          if (this.dashboardData.project) {
            this.form.controls.project.setValue(this.dashboardData.project);
          } else if (this.dashboardData?.defaultProject) {
            this.form.controls.project.setValue(this.dashboardData.defaultProject);
          }
        },
        error: err => {
          console.log(err);
        },
      });
    } else {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load user' });
    }
  }

  onSubmit() {
    this.dashboardControllerService.updateDashboardData({
      visibility: this.form.controls.visibility.value,
      groupxProject: this.form.controls.project.value,
    }).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Dashboard data updated successfully',
        });
      },
      error: err => {
        console.log(err);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update dashboard data' });
      },
    });
  }
}
