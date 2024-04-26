import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UsersService} from "../_services/users.service";
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {MessageModule} from "primeng/message";
import {ChipsModule} from "primeng/chips";
import {ButtonModule} from "primeng/button";
import {MessagesModule} from "primeng/messages";
import {Message} from "primeng/api";

@Component({
  selector: 'app-validation',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    MessageModule,
    ChipsModule,
    ButtonModule,
    MessagesModule
  ],
  templateUrl: './validation.component.html',
  styleUrl: './validation.component.css'
})
export class ValidationComponent {
  userId: string = "";
  validated: boolean = false;
  messages: | undefined;

  constructor(private route: ActivatedRoute, private usersService: UsersService) {
  }
  ngOnInit() {

  }

  validateUser(username: string, password: string) {
    if(username === undefined || password === undefined) {
      console.error("Username or password is undefined");
      return;
    }
    this.userId = username;
    this.usersService.validateUser(username, password).subscribe({
      next: (data) => {
        if(data !== null) {
          this.validated = true;
        }
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      }
    });
  }

  setPassword(password: string, passwordRepeat: string) {
    if (password === passwordRepeat) {
      this.usersService.enableUser(this.userId, password).subscribe({
        next: (data) => {
          if(data !== null) {
          }
        },
        error: (error) => {
          console.error('Failed to load user details:', error);
        }
      });
    } else {
      console.error("Passwords do not match");
    }
  }

}
