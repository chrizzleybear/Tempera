import {Component, OnInit} from '@angular/core';
import {UsersService} from '../../_services/users.service';
import {NgForOf} from "@angular/common";
import {User} from "../../models/user.model";
import { ListboxModule } from 'primeng/listbox';
@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    NgForOf,
    ListboxModule
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit{

  users: User[] = [];
  userListboxOptions: any[] = [];
  constructor(private usersService: UsersService) {

  }
  ngOnInit(): void {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.userListboxOptions = this.users.map(user => ({ label: `${user.username} - ${user.firstName} ${user.lastName}`, value: user.id
      }));
    });
  }


}
