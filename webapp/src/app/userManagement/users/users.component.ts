import {Component, OnInit} from '@angular/core';
import {UsersService} from '../../_services/users.service';
import {NgForOf} from "@angular/common";
import {User} from "../../models/user.model";
import {TableModule} from 'primeng/table';
import {InputTextModule} from "primeng/inputtext";
import { forkJoin } from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    NgForOf,
    TableModule,
    InputTextModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit{

  users: User[] = [];
  filteredUsers: User[] = [];
  selectedUsers: User[] = []

  constructor(private usersService: UsersService, private router: Router, private route: ActivatedRoute ) {

  }
  ngOnInit(): void {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
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
    });
    forkJoin([this.usersService.getAllUsers()]).subscribe(([users]) => {
      this.users = users;
      this.filteredUsers = users;
    }
    );
  }

  updateSelectedUsers(user: User): void {
    this.usersService.updateUser(user);
  }

  viewUserDetails(user: any) {
    this.router.navigate(['/user', user.id]);
    console.log(user);
  }

}
