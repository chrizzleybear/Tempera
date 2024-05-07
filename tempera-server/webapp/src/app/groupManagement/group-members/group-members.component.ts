import {Component, OnInit} from '@angular/core';
import {User} from "../../models/user.model";
import {ActivatedRoute} from "@angular/router";
import {GroupService} from "../../_services/group.service";
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {UsersService} from "../../_services/users.service";
import {DialogModule} from "primeng/dialog";
import {MessagesModule} from "primeng/messages";
import {GroupMemberDTO} from "../../models/groupDtos";
@Component({
  selector: 'app-group-members',
  standalone: true,
  imports: [
    SharedModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    DialogModule,
    MessagesModule
  ],
  templateUrl: './group-members.component.html',
  styleUrl: './group-members.component.css'
})
export class GroupMembersComponent implements OnInit{

  members: User[] = [];
  users: User[] = [];
  displayAddDialog: boolean = false;
  groupId: string | null | undefined;
  filteredMembers: User[] = [];
  filteredUsers: User[] = [];
  selectedUsers: User[] = [];
  messages: any;

  constructor(private groupService: GroupService, private userService: UsersService ,private route: ActivatedRoute) {

  }
  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get('id');
    this.loadMembersAndUsers(this.groupId!);
  }


  loadMembersAndUsers(groupId: string) {
    // Load members
    this.groupService.getGroupMembers(Number(groupId)).subscribe({
      next: members => {
        this.members = members;
        this.filteredMembers = [...members];
        // Load all users
        this.userService.getAllUsers().subscribe({
          next: users => {
            this.users = users.filter((user: { username: string; }) =>
              !this.members.some(member => member.username === user.username));
              this.filteredUsers = [...this.users];
          },
          error: err => console.error("Error loading users:", err)
        });
      },
      error: err => console.error("Error loading group members:", err)
    });
  }

  applyFilterMembers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredMembers = this.members.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredMembers = this.members;
    }
  }

  applyFilterUsers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredUsers = this.users;
    }
  }
  addMemberDialog(){
    this.loadMembersAndUsers(this.groupId!);
    this.displayAddDialog = true;
  }

  addMember(userId: string) {
    const dto: GroupMemberDTO = {
      groupId: Number(this.groupId),
      memberId: userId
    }
    this.groupService.addGroupMember(dto).subscribe({
      next: response => {
        console.log("Member added successfully:", response);
      },
      error: err => console.error("Error adding member:", err)
    });
    }

  addMembers() {
    this.selectedUsers.forEach(user => {
      this.addMember(user.username);
    });
    this.selectedUsers = [];
    this.displayAddDialog = false;
    this.loadMembersAndUsers(this.groupId!);
    this.messages = [{severity:'success', summary:'Success', detail:'Members added successfully'}];
  }

    deleteMember(userId: string) {
    this.groupService.deleteGroupMember(Number(this.groupId!), userId).subscribe({
      next: response => {
        console.log("Member deleted successfully:", response);
        this.loadMembersAndUsers(this.groupId!);
        this.messages = [{severity:'success', summary:'Success', detail:'Member deleted successfully'}];
      },
      error: err => console.error("Error deleting member:", err)
    });
    }

}
