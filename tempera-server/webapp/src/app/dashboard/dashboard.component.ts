import { Component, OnInit } from '@angular/core';
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
import { ColleagueStateDto, DashboardControllerService, DashboardDataResponse, UserxDto } from '../../api';
import StateEnum = ColleagueStateDto.StateEnum;
import VisibilityEnum = DashboardDataResponse.VisibilityEnum;
import { StorageService } from '../_services/storage.service';
import { ButtonModule } from 'primeng/button';
import RolesEnum = UserxDto.RolesEnum;
import { WrapFnPipe } from '../_pipes/wrap-fn.pipe';

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
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent implements OnInit {
  public dashboardData?: DashboardDataResponse;
  public user?: User;

  public filterFields: string[] = [];

  // todo: delete when actual data is available
  public visibilityOptions: VisibilityEnum[] = Object.values(VisibilityEnum);

  protected readonly RolesEnum = RolesEnum;

  constructor(private dashboardControllerService: DashboardControllerService, private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.user = this.storageService.getUser();

    if (this.user) {
      this.dashboardControllerService.dashboardData(this.user.username).subscribe({
        next: data => {
          this.dashboardData = data;
          this.filterFields = Object.keys(this.dashboardData?.colleagueStates?.[0] ?? []);
        },
        error: err => {
          console.log(err);
        },
      });
    }
  }

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
}
