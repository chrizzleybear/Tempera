import {Component, OnInit} from '@angular/core';
import {UsersService} from '../../_services/users.service';
import {NgForOf, NgIf} from "@angular/common";
import {User} from "../../models/user.model";
import {TableModule} from 'primeng/table';
import {InputTextModule} from "primeng/inputtext";
import { forkJoin } from 'rxjs';
import {Router} from '@angular/router';
import {ButtonModule} from "primeng/button";
import {UserEditComponent} from "../user-edit/user-edit.component";
import {DialogModule} from "primeng/dialog";
import {UserCreateComponent} from "../user-create/user-create.component";
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
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit{

  users: User[] = [];
  filteredUsers: User[] = [];
  selectedUsers: User[] = []
  displayEditDialog: boolean = false;
  selectedUser: any;
  displayCreateDialog: boolean = false;
  messages: any;

  constructor(private usersService: UsersService, private router: Router ) {

  }
  ngOnInit(): void {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
    this.selectedUser = {};
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase())
      );
    } else {
      this.filteredUsers = this.users;
    }
  }

  deleteSelectedUsers(): void {
    console.log("delete selected users");
    this.selectedUsers.forEach(user => {
      this.usersService.deleteUser(user.id);
      this.messages = [{severity:'success', summary:'Success', detail:'User deleted successfully'}];
      this.returnToUsers();
    });
    forkJoin([this.usersService.getAllUsers()]).subscribe(([users]) => {
      this.users = users;
      this.filteredUsers = users;
    }
    );
  }

  viewUserDetails(user: any) {
    this.router.navigate(['/user', user.id]);
    console.log(user);
  }

  editUser(user: any) {
    //this.router.navigate(['user/edit', user.id]);
    this.selectedUser = user;
    this.displayEditDialog = true;
    console.log(user);

  }
  createUser() {
    this.displayCreateDialog = true;

  }
  returnToUsers() {
    this.displayEditDialog = false;
    this.displayCreateDialog = false;
    this.ngOnInit();
  }
  onEditCompleted(success: boolean) {
    if (success) {
      this.messages = [{severity:'success', summary:'Success', detail:'User updated successfully'}];
      this.returnToUsers();
    }
  }

  onCreateCompleted(success: boolean) {
    if (success) {
      this.messages = [{severity:'success', summary:'Success', detail:'User created successfully'}];
      this.returnToUsers();
    }
  }
}
