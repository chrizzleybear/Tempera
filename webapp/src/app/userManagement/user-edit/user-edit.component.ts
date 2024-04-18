import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import { UsersService } from '../../_services/users.service';
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from "@angular/common";
import {DialogModule} from "primeng/dialog";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf, DialogModule]
})
export class UserEditComponent implements OnInit {
  userForm: FormGroup;
  username: any;
  roles: string[];
  @Input() user: any;
  @Output() editCompleted = new EventEmitter<boolean>();


  constructor(private fb: FormBuilder, private usersService: UsersService) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', "GROUPLEAD"];
    this.userForm = this.fb.group({
      username: [''],
      password: [''],
      firstName: [''],
      lastName: [''],
      email: [''],
      enabled: [''],
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEAD: false
      }),
    });
  }

  ngOnInit() {
    //this.username = this.route.snapshot.paramMap.get('id');
    this.username = this.user.username;
    this.usersService.getUserById(this.username).subscribe({
      next: (data) => {
        this.user = data;
        this.populateForm();
      },
      error: (error) => {
        console.error('Failed to load user details:', error);
      }
    });
  }

  private populateForm() {
    if (this.user) {
      this.userForm.patchValue({
        username: this.user.username,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        password: this.user.password,
        email: this.user.email,
        enabled: this.user.enabled
      });
      const rolesControl = this.userForm.get('roles') as FormGroup;
      this.roles.forEach(role => {
        rolesControl.get(role)?.setValue(this.user.roles.includes(role));
      });
    }
  }
  onSubmit() {
    this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
    console.log(this.userForm.value);
    this.usersService.updateUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User updated successfully:', response);
        this.editCompleted.emit(true);
      },
      error: (error) => {
        console.error('Error updating user:', error);
        this.editCompleted.emit(false);
      }
    });
  }

}
