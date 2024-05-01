import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { State } from '../models/user.model';
import { StorageService } from '../_services/storage.service';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { HomeData } from '../models/home-data.model';
import { DropdownModule } from 'primeng/dropdown';
import { DateTime } from 'luxon';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIf,
    MessageModule,
    DropdownModule,
    TableModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  public homeData?: HomeData;

  showState(state: State | undefined) {
    switch (state) {
      case State.AVAILABLE:
        return 'Available';
      case State.MEETING:
        return 'In a meeting';
      case State.DEEP_WORK:
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

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getHomeData().subscribe( {
      next: data => {
        this.homeData = data;
        console.log(data);
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
