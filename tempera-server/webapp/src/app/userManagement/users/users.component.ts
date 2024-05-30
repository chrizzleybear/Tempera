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
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
})
/**
 * @class UsersComponent
 * This component is responsible for managing all users.
 */
export class UsersComponent implements OnInit {

  users: User[] = [];
  filteredUsers: User[] = [];
  displayEditDialog: boolean = false;
  selectedUser: any;
  displayCreateDialog: boolean = false;
  messages: any;

  constructor(private usersService: UsersService, private router: Router) {
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
   * Deletes a user based on the user ID.
   * @param userId The ID of the user to delete.
   */
  deleteSelectedUser(userId: string): void {
    console.log('Delete user with ID: ', userId);
    this.usersService.deleteUser(userId).subscribe({
      next: (response) => {
        this.messages = [{ severity: 'success', summary: 'Success', detail: 'User deleted successfully' }];
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error deleting user:', error);
        this.messages = [{ severity: 'error', summary: 'Error', detail: 'Error deleting user' }];
      },
    });
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
      this.messages = [{ severity: 'success', summary: 'Success', detail: 'User updated successfully' }];
      this.returnToUsers();
    }
  }

  onCreateCompleted(success: boolean) {
    if (success) {
      this.messages = [{ severity: 'success', summary: 'Success', detail: 'User created successfully' }];
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

}
