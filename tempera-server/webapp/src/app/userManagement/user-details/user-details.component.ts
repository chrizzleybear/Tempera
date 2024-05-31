import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UsersService } from '../../_services/users.service';
import { NgForOf, NgIf } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { User } from '../../models/user.model';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    ReactiveFormsModule,
    CardModule,
  ],
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
})
/**
 * @class UserDetails
 * This component is responsible for displaying user details.
 * It fetches the user details from the backend and displays them.
 */
export class UserDetailsComponent implements OnInit {
  user: User | undefined;
  userId: string | null | undefined;

  constructor(
    private route: ActivatedRoute,
    private usersService: UsersService,
  ) {
  }

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id');
    if (this.userId) {
      this.fetchUserDetails(this.userId);
    }
  }

  fetchUserDetails(id: string) {
    this.usersService.getUserById(id).subscribe({
      next: (data) => {
        this.user = data;
        console.log('User details: ', this.user);
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      },
    });
  }
}
