import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsersService } from '../../_services/users.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { NgIf } from '@angular/common';
import { MessageModule } from 'primeng/message';
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    NgIf,
    MessageModule,
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css',
})
/**
 * @class UserCreateComponent
 * This component is used for creating a new user.
 */
export class UserCreateComponent {
  userForm: FormGroup;
  roles: string[];
  @Output() creatComplete = new EventEmitter<boolean>();

    /**
     * Constructor for UserCreateComponent that initializes the create form.
     * @param fb to create the form
     * @param usersService
     * @param messageService
     */
  constructor(private fb: FormBuilder, private usersService: UsersService, private messageService: MessageService) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', 'GROUPLEAD'];
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      enabled: false,
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEAD: false,
      }),
    });
  }

    /**
     * Method to create a new user.
     * Password is not sent to the backend, because it is generated.
     * It validates the form and sends the data to the backend.
     */
  onSubmit() {
    if (this.userForm.valid) {
      this.userForm.value.password = '';
      this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
      console.log(this.userForm.value);
      this.usersService.saveUser(this.userForm.value).subscribe({
        next: (response) => {
            console.log('User created:', response);
            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'User created successfully' });
            this.userForm.reset();
            this.creatComplete.emit(true);
        },
        error: (error) => {
          console.error('Error updating user:', error);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: error.error.message });
          this.creatComplete.emit(false);
        },
      });
    } else {
      console.error('Invalid form');
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid form' });
    }
  }
}
