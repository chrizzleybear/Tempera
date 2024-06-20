import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import { ChipsModule } from 'primeng/chips';
import { ButtonModule } from 'primeng/button';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';
import { UserManagementControllerService } from '../../api';

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
  enabled: boolean = false;
  messages: Message[] = [];

  constructor(private usersService: UserManagementControllerService) {
  }

  /**
   * Sets a new password to enable user.
   * @param username The username of the user.
   * @param token The token that the user received via email.
   * @param password
   * @param passwordRepeat
   */
  setPassword(username: string, token: string, password: string, passwordRepeat: string) {
    if (password === passwordRepeat) {
      this.usersService.enableUser({username: username,token: token , password: password}).subscribe({
        next: (data) => {
          if (data !== null) {
            this.enabled = true;
            this.messages = [{ severity: 'success', summary: 'Success', detail: 'User enabled' }];
          } else {
            this.messages = [{ severity: 'error', summary: 'Error', detail: 'Failed to enable user' }];
          }
        },
        error: () => {
          this.messages = [{ severity: 'error', summary: 'Error', detail: 'Failed to enable user with this combination of username and token' }];
        }
      });
    } else {
      this.messages = [{ severity: 'error', summary: 'Error', detail: 'Passwords do not match' }];
      console.error('Passwords do not match');
    }
  }

}
