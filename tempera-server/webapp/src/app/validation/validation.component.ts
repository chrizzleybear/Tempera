import { Component } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UsersService } from '../_services/users.service';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { ChipsModule } from 'primeng/chips';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';

@Component({
  selector: 'app-validation',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    MessageModule,
    ChipsModule,
    ButtonModule,
    MessagesModule,
    RouterLink,
  ],
  templateUrl: './validation.component.html',
  styleUrl: './validation.component.css',
})
/**
 * @class ValidationComponent
 * This component is responsible for user validation and enabling a user.
 */
export class ValidationComponent {
  userId: string = '';
  validated: boolean = false;
  enabled: boolean = false;
  messages: Message[] = [];

  constructor(private route: ActivatedRoute, private usersService: UsersService) {
  }

  /**
   * Validates a user's credentials.
   * @param username The username of the user.
   * @param password The password of the user.
   * @returns void
   */
  validateUser(username: string, password: string) {
    if (username === undefined || password === undefined) {
      console.error('Username or password is undefined');
      return;
    }
    this.userId = username;
    this.usersService.validateUser(username, password).subscribe({
      next: (data) => {
        if (data !== null) {
          this.validated = true;
          this.messages = [{ severity: 'success', summary: 'Success', detail: 'You have been validated' }];
        } else {
          this.messages = [{ severity: 'error', summary: 'Error', detail: 'Wrong username or token' }];
        }
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      },
    });
  }

  /**
   * Sets a new password to enable user.
   * @param password
   * @param passwordRepeat
   */
  setPassword(password: string, passwordRepeat: string) {
    if (password === passwordRepeat) {
      this.usersService.enableUser(this.userId, password).subscribe({
        next: (data) => {
          if (data !== null) {
            this.enabled = true;
            this.messages = [{ severity: 'success', summary: 'Success', detail: 'User enabled' }];
          } else {
            this.messages = [{ severity: 'error', summary: 'Error', detail: 'Failed to enable user' }];
          }
        },
      });
    } else {
      this.messages = [{ severity: 'error', summary: 'Error', detail: 'Passwords do not match' }];
      console.error('Passwords do not match');
    }
  }

}
