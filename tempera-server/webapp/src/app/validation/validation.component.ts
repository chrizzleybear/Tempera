import { Component } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UsersService} from "../_services/users.service";

@Component({
  selector: 'app-validation',
  standalone: true,
  imports: [],
  templateUrl: './validation.component.html',
  styleUrl: './validation.component.css'
})
export class ValidationComponent {

  user: any;
  userId: string | null | undefined;
  validated: boolean = false;

  constructor( private route: ActivatedRoute, private usersService: UsersService) {
  }
  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id');
    if (this.userId) {
      this.fetchUserDetails(this.userId);
    }
  }
  fetchUserDetails(id: string) {
    if (this.userId) {
      this.usersService.getUserById(this.userId).subscribe({
        next: (data) => {
          this.user = data;
          console.log("User details: ", this.user);
        },
        error: (error) => {
          console.error('Failed to load user details:', error);
        }
      });
    }
  }

  validateUser(password: string, username: string) {
    if (this.user.username === username && this.user.password === password) {
      this.validated = true;
    } else {
      console.error("User validation failed");
    }
  }

  setPassword(password: string, passwordRepeat: string) {
    if (password === passwordRepeat) {
      this.user.password = password;
      this.usersService.updateUser(this.user);
    } else {
      console.error("Passwords do not match");
    }
  }

}
