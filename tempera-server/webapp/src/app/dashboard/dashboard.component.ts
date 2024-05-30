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
  SimpleGroupxProjectDto,
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

  public form = new FormGroup({
    visibility: new FormControl<VisibilityEnum>(VisibilityEnum.Public, { nonNullable: true }),
    project: new FormControl<SimpleGroupxProjectDto | undefined>(undefined, { nonNullable: true }),
  });

  constructor(private dashboardControllerService: DashboardControllerService, private storageService: StorageService, private destroyRef: DestroyRef, private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.user = this.storageService.getUser();

    if (this.user) {
      this.getData$(this.user.username).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: data => {
          this.dashboardData = data;
          this.colleagueTableFilterFields = Object.keys(this.dashboardData?.colleagueStates?.[0] ?? []);

          // set form values in case of existing data
          this.form.controls.visibility.setValue(this.dashboardData.visibility);
          if (this.user?.roles?.includes(RolesEnum.Admin)) {
            this.form.controls.visibility.disable();
          }
          if (this.dashboardData?.defaultProject?.projectId) {
            this.form.controls.project.setValue(this.dashboardData?.defaultProject);
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
      next: data => {
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
