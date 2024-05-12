import { Component, DestroyRef, OnInit } from '@angular/core';
import { State, User } from '../models/user.model';
import { DatePipe, NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AirQualityPipe } from '../_pipes/air-quality.pipe';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';
import { ColleagueStateDto, DashboardControllerService, DashboardDataResponse, ProjectDto, UserxDto } from '../../api';
import StateEnum = ColleagueStateDto.StateEnum;
import VisibilityEnum = DashboardDataResponse.VisibilityEnum;
import { StorageService } from '../_services/storage.service';
import { ButtonModule } from 'primeng/button';
import RolesEnum = UserxDto.RolesEnum;
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MessagesModule } from 'primeng/messages';
import { map, Observable, switchMap, timer } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

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
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  public dashboardData?: DashboardDataResponse;

  public user?: User;

  public colleagueTableFilterFields: string[] = [];

  public visibilityOptions: VisibilityEnum[] = Object.values(VisibilityEnum);

  public messages: any;

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

  protected readonly RolesEnum = RolesEnum;

  public form = new FormGroup({
    visibility: new FormControl<VisibilityEnum>(VisibilityEnum.Public, { nonNullable: true }),
    project: new FormControl<ProjectDto | undefined>(undefined, { nonNullable: true }),
  });

  constructor(private dashboardControllerService: DashboardControllerService, private storageService: StorageService, private destroyRef: DestroyRef) {
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
          if (this.dashboardData?.defaultProject?.id) {
            this.form.controls.project.setValue(this.dashboardData?.defaultProject);
          }
        },
        error: err => {
          console.log(err);
        },
      });
    } else {
      this.messages = [{ severity: 'error', summary: 'Error', detail: 'Failed to load user' }];
    }
  }

  /**
   * Get color of colleague state badges
   */
  getSeverity(colleague: ColleagueStateDto) {
    if (!colleague.isVisible) {
      return 'primary';
    }
    switch (colleague.state) {
      case State.AVAILABLE:
        return 'success';
      case State.MEETING:
        return 'warning';
      case State.DEEPWORK:
        return 'info';
      case State.OUT_OF_OFFICE:
        return 'danger';
      default:
        return 'primary';
    }
  }

  /**
   * Get display text for colleague state
   */
  showState(state: StateEnum | undefined) {
    switch (state) {
      case StateEnum.Available:
        return 'Available';
      case StateEnum.Meeting:
        return 'In a meeting';
      case StateEnum.Deepwork:
        return 'Deep work';
      case StateEnum.OutOfOffice:
        return 'Out of office';
      default:
        return 'Unknown';

    }
  }

  /**
   * Get display text for colleague state but also consider visibility
   */
  showColleagueState(colleague: ColleagueStateDto | undefined) {
    if (!colleague?.isVisible) {
      return 'Hidden';
    }
    switch (colleague.state) {
      case StateEnum.Available:
        return 'Available';
      case StateEnum.Meeting:
        return 'In a meeting';
      case StateEnum.Deepwork:
        return 'Deep work';
      case StateEnum.OutOfOffice:
        return 'Out of office';
      default:
        return 'Unknown';
    }
  }

  onSubmit() {
    this.dashboardControllerService.updateDashboardData({
      visibility: this.form.controls.visibility.value,
      project: this.form.controls.project.value,
    }).subscribe({
      next: data => {
        this.messages = [{ severity: 'success', summary: 'Success', detail: 'Dashboard data updated successfully' }];
      },
      error: err => {
        console.log(err);
        this.messages = [{ severity: 'error', summary: 'Error', detail: 'Failed to update dashboard data' }];
      },
    });
  }
}
