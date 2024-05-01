import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { State } from '../models/user.model';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { HomeData } from '../models/home-data.model';
import { DropdownModule } from 'primeng/dropdown';
import { DateTime } from 'luxon';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIf,
    MessageModule,
    DropdownModule,
    TableModule,
    TagModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  public homeData?: HomeData;


  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getHomeData().subscribe({
      next: data => {
        this.homeData = data;
        console.log(data);
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

  getTime(): string {
    if (!this.homeData?.stateTimestamp) {
      return 'Unknown';
    }
    let time = DateTime.fromISO(this.homeData.stateTimestamp);

    return time.toFormat('HH:mm');
  }
}
