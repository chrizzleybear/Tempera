import { Component, OnInit } from '@angular/core';
import { HomeService } from '../_services/home.service';
import { State } from '../models/user.model';
import { DatePipe, NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { HomeData } from '../models/home-data.model';
import { DropdownModule } from 'primeng/dropdown';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { AirQualityPipe } from '../_pipes/air-quality.pipe';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-home',
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
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  public homeData?: HomeData;

  public filterFields: string[] = [];

  constructor(private homeService: HomeService) {
  }

  ngOnInit(): void {
    this.homeService.getHomeData().subscribe({
      next: data => {
        this.homeData = data;
        this.filterFields = Object.keys(this.homeData?.colleagueStates[0] ?? []);
      },
      error: err => {
        console.log(err);
      },
    });
  }

  getSeverity(state: State) {
    switch (state) {
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

  showState(state: State | undefined) {
    switch (state) {
      case State.AVAILABLE:
        return 'Available';
      case State.MEETING:
        return 'In a meeting';
      case State.DEEPWORK:
        return 'Deep work';
      case State.OUT_OF_OFFICE:
        return 'Out of office';
      default:
        return 'Unknown';

    }
  }
}
