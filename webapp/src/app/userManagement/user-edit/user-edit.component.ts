import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import { UsersService } from '../../_services/users.service';
import {ActivatedRoute} from "@angular/router";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, NgForOf]
})
export class UserEditComponent implements OnInit {
  userForm: FormGroup;
  private user: any;
  userId: any;
  roles: string[];


  constructor(private fb: FormBuilder, private usersService: UsersService, private route: ActivatedRoute) {
    this.roles = ['ADMIN', 'EMPLOYEE', 'MANAGER', "GROUPLEADER"];
    this.userForm = this.fb.group({
      userId: [''],
      password: [''],
      firstName: [''],
      lastName: [''],
      email: [''],
      enabled: [''],
      roles: this.fb.group({
        ADMIN: false,
        EMPLOYEE: false,
        MANAGER: false,
        GROUPLEADER: false
      }),
    });
  }

  ngOnInit() {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.usersService.getUserById(this.userId).subscribe({
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
        userId: this.user.username,
        firstName: this.user.firstName,
        lastName: this.user.lastName,
        password: this.user.password,
        email: this.user.email,
        enabled: this.user.enabled,
        roles: this.buildRoles()
      });
    }
  }

  buildRoles() {
    const hasroles = [false, false, false, false];
    if(this.user.roles) this.user.roles.forEach((role: string) => {
      if (role === 'ADMIN') hasroles[0] = true;
      if (role === 'EMPLOYEE') hasroles[1] = true;
      if (role === 'MANAGER') hasroles[2] = true;
      if (role === 'GROUPLEADER') hasroles[3] = true;
    });
    return new FormGroup({
      ADMIN: new FormControl(hasroles[0]),
      EMPLOYEE: new FormControl(hasroles[1]),
      MANAGER: new FormControl(hasroles[2]),
      GROUPLEADER: new FormControl(hasroles[3])
    });
  }
  onSubmit() {
    this.userForm.value.roles = Object.keys(this.userForm.value.roles).filter((role) => this.userForm.value.roles[role]);
    console.log(this.userForm.value);
    this.usersService.updateUser(this.userForm.value).subscribe({
      next: (response) => {
        console.log('User updated successfully:', response);
      },
      error: (error) => {
        console.error('Error updating user:', error);
      }
    });
  }

}
