import {Component, OnInit} from '@angular/core';
import {UsersService} from '../../_services/users.service';
import {NgForOf, NgIf} from "@angular/common";
import {User} from "../../models/user.model";
import {TableModule} from 'primeng/table';
import {InputTextModule} from "primeng/inputtext";
import {Router} from '@angular/router';
import {ButtonModule} from "primeng/button";
import {UserEditComponent} from "../user-edit/user-edit.component";
import {DialogModule} from "primeng/dialog";
import {UserCreateComponent} from "../user-create/user-create.component";
import { MessagesModule } from 'primeng/messages';
import {OverlayPanelModule} from "primeng/overlaypanel";

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
    this.loadUsers();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase())
      );
    } else {
      this.filteredUsers = this.users;

      OverlayPanelModule
    }
  }

  deleteSelectedUsers(userId: string ): void {
      console.log("Delete user with ID: ", userId);
      this.usersService.deleteUser(userId);
      this.messages = [{severity:'success', summary:'Success', detail:'User deleted successfully'}];
      this.loadUsers();
  }

  loadUsers() {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
      console.log("update");
    });
  }
  editUser(user: any) {
    this.selectedUser = { ...user };
    this.displayEditDialog = true;
    console.log("detail", user);

  }
  createUser() {
    this.displayCreateDialog = true;

  }
  returnToUsers() {
    this.loadUsers();
    this.displayEditDialog = false;
    this.displayCreateDialog = false;

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
  viewUserDetails(userId: String) {
    this.router.navigate(['/user', userId]);
    console.log(userId);
  }

}
