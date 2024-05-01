import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { User } from '../models/user.model';
import { StorageService } from '../_services/storage.service';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import {HomeData} from "../models/home-data.model";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    NgIf,
    MessageModule,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  content?: string;

  user?: User;

  homeData?: HomeData;

  constructor(private userService: UserService, private storageService: StorageService) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();


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
