import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsersService } from '../../_services/users.service';
import { NgForOf, NgIf } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { User } from '../../models/user.model';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf, DialogModule, InputTextModule, ButtonModule, MessageModule, NgIf],
})
/**
 * @class UserEditComponent
 * This component is responsible for editing a user.
 */
export class UserEditComponent implements OnInit, OnChanges {
  userForm: FormGroup;
  username!: string;
  roles: string[];
  @Input({ required: true }) user!: User; // The user to edit from the users component
  @Output() editCompleted = new EventEmitter<boolean>(); // Event emitter to notify the parent component that the edit is completed

  /**
   * Constructor for UserEditComponent that initializes the edit form.
   * @param fb
   * @param usersService
   * @param messageService
   */
  constructor(private fb: FormBuilder, private usersService: UsersService, private messageService: MessageService) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', 'GROUPLEAD'];
    this.userForm = this.fb.group({
      username: '',
      password: ['', Validators.minLength(6)],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.email],
      enabled: '',
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEAD: false,
      }),
    });
  }

  ngOnInit() {
    this.username = this.user.username;
    this.usersService.getUserById(this.username).subscribe({
      next: (data) => {
        this.user = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to load user details' });
      },
    });
  }

  /**
   * Called when the input properties of the component change.
   * To check if the current value is not stored in the user object.
   * @param changes
   */
  ngOnChanges(changes: SimpleChanges) {
    // Check if 'user' input has changed
    if (changes['user']?.currentValue) {
      this.username = this.user.username;
      this.populateForm();
    }
  }

  private populateForm() {
    if (this.user) {
      this.userForm.patchValue({
        username: this.user.username,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        password: this.user.password,
        email: this.user.email,
        enabled: this.user.enabled,
      });
      const rolesControl = this.userForm.get('roles') as FormGroup;
      // Set the roles to true if the user has the role.
      this.roles.forEach(role => {
        rolesControl.get(role)?.setValue(this.user.roles.includes(role));
      });
    }
  }

  /**
   * Submits the user form to update the user.
   * Roles are filtered to only include the selected roles.
   */
  onSubmit() {
    this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
    console.log(this.userForm.value);
    this.usersService.updateUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User updated successfully:', response);
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'User updated successfully' });
        this.editCompleted.emit(true);
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating user' });
        this.editCompleted.emit(false);
      },
    });
  }

}
