import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {UsersService} from "../../_services/users.service";

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css'
})
export class UserCreateComponent {
  userForm: FormGroup;
  roles: string[];
  @Output() creatComplete = new EventEmitter<boolean>();
  constructor(private fb: FormBuilder, private usersService: UsersService) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', "GROUPLEAD"];
    this.userForm = this.fb.group({
      username: [''],
      password: [''],
      firstName: [''],
      lastName: [''],
      email: [''],
      enabled: false,
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEAD: false
      }),
    });
  }

  ngOnInit() {

  }

  onSubmit() {
    this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
    console.log(this.userForm.value);
    this.usersService.saveUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User updated successfully:', response);
        this.creatComplete.emit(true);
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.creatComplete.emit(false);
      }
    });
  }
}
