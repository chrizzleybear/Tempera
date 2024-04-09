import {Component, OnInit, ViewChild} from '@angular/core';
import {UsersService} from '../../_services/users.service';
import {NgForOf} from "@angular/common";
import {User} from "../../models/user.model";
import {Table, TableModule} from 'primeng/table';
import {InputTextModule} from "primeng/inputtext";

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
  @ViewChild('dt') table!: Table;

  constructor(private usersService: UsersService) {

  }
  ngOnInit(): void {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
    });
  }

  applyFilter($event: Event) {
    const filterValue = ($event.target as HTMLInputElement).value;
    this.table.filterGlobal(filterValue, 'contains');
  }
}
