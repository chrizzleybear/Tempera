import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../_services/users.service';
import { NgForOf, NgIf } from '@angular/common';
import { User } from '../../models/user.model';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { UserEditComponent } from '../user-edit/user-edit.component';
import { DialogModule } from 'primeng/dialog';
import { UserCreateComponent } from '../user-create/user-create.component';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from "primeng/toast";
import { MessageService } from "primeng/api";
import { DeletionResponseDto, UserManagementControllerService, UserxDto } from '../../../api';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    NgForOf,
    TableModule,
    InputTextModule,
    ButtonModule,
    NgIf,
    UserEditComponent,
    DialogModule,
    UserCreateComponent,
    MessagesModule,
    ToastModule,
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
})
/**
 * @class UsersComponent
 * This component is responsible for managing all users.
 */
export class UsersComponent implements OnInit {

  users: UserxDto[] = [];
  filteredUsers: UserxDto[] = [];
  displayEditDialog: boolean = false;
  selectedUser: any;
  displayCreateDialog: boolean = false;
  displayDeletionPopup: boolean = false;
  deletionResponse: DeletionResponseDto | null;
  deletionResponseMessage: string = '';

  constructor(private usersService: UserManagementControllerService, private router: Router, private messageService: MessageService) {
    this.deletionResponse = null;
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  /**
   * Filters users based on the input value.
   * @param event
   */
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredUsers = this.users;
    }
  }

  /**
   * Deletes a user based on the user ID. If deletion is successful, a success message is displayed.
   * If deletion fails, an error message with a hint, why it failed is displayed.
   * @param userId The ID of the user to delete.
   */
  deleteSelectedUser(userId: string): void {
    console.log('Delete user with ID: ', userId);
    this.usersService.deleteUser(userId).subscribe({
      next: (response) => {
        console.log('User deleted:', response);
        this.evaluateResponse(response);
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error deleting user:', error);
        this.messageService.add({severity:'error', summary:'Error', detail:'Error deleting user'});
      },
    });
  }

  /**
   * Evaluates the response of an API call and displays a success or error message.
   * @param response The response of the API call.
   */
  evaluateResponse(response: DeletionResponseDto): void {
    if (response.responseType === 'SUCCESS') {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'User deleted' });
    } else if (response.responseType === 'ERROR') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'User could not be deleted' });
    } else if (response.responseType === 'MANAGER') {
      this.deletionResponse = response;
      // Now we can build our own deletion Response (define it in the component, so that it can store
      // the affected Projects and Groups and the message) -> and then display it in the dialog
      this.deletionResponseMessage = "The user could not be deleted, because he is a manager of the following projects:"
    } else if (response.responseType === 'GROUPLEAD') {
      this.deletionResponseMessage = "The user could not be deleted, because he is a group lead of the following groups:"
    }else if (response.responseType === 'ADMIN') {
      this.deletionResponseMessage = "The user could not be deleted, because he is an admin."
    }
      this.displayDeletionPopup = true;
    }

  loadUsers() {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
  }

  /**
   * Opens the edit user dialog.
   * @param user
   */
  editUser(user: any) {
    this.selectedUser = { ...user };
    this.displayEditDialog = true;
    console.log('detail', user);

  }

  /**
   * Opens the create user dialog.
   */
  createUser() {
    this.displayCreateDialog = true;

  }

  /**
   * Closes the edit or create user dialog to go back to overview.
   */
  returnToUsers() {
    this.loadUsers();
    this.displayEditDialog = false;
    this.displayCreateDialog = false;

  }

  onEditCompleted(success: boolean) {
    if (success) {
      this.returnToUsers();
    }
  }

  onCreateCompleted(success: boolean) {
    if (success) {
      this.returnToUsers();
    }

  }

  /**
   * Navigates to the user details page.
   * @param userId The ID of the user to view details of.
   */
  viewUserDetails(userId: string) {
    this.router.navigate(['/user', userId]);
  }

  protected readonly DeletionResponseDto = DeletionResponseDto;
}
