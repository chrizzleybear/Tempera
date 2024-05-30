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
import { from } from 'rxjs';
import { concatMap } from 'rxjs/operators';

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
/**
 * @class GroupMembersComponent
 * This component is responsible for managing group members.
 */
export class GroupMembersComponent implements OnInit{

  members: User[] = [];
  users: User[] = [];
  displayAddDialog: boolean = false;
  groupId: number | null | undefined;
  groupName: string | null | undefined;
  filteredMembers: User[] = [];
  filteredUsers: User[] = [];
  selectedUsers: User[] = [];
  messages: any;

  constructor(private groupService: GroupService, private userService: UsersService ,private route: ActivatedRoute) {

  }
  ngOnInit(): void {
    this.groupId = Number(this.route.snapshot.paramMap.get('id'));
    this.groupName = this.route.snapshot.paramMap.get('name');
    this.loadMembersAndUsers(this.groupId!);
  }

  /**
   * Load all members and users that are not members of the group.
   * @param groupId
   */
  loadMembersAndUsers(groupId: number) {
    // Load members
    this.groupService.getGroupMembers(groupId).subscribe({
      next: members => {
        this.members = members;
        this.filteredMembers = [...members];
        // Load all users that are not members
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
  addMembers() {
    from(this.selectedUsers.map(user => user.username))
      .pipe(concatMap(userId => this.addMember(userId)))
      .subscribe({
        next: response => {
          console.log("Member added successfully:", response);
          this.loadMembersAndUsers(this.groupId!);
          this.messages = [{severity:'success', summary:'Success', detail:'Members added successfully'}];
        },
        error: err => console.error("Error adding member:", err)
      });
    this.displayAddDialog = false;
    this.selectedUsers = [];
  }
  private addMember(userId: string) {
    const dto: GroupMemberDTO = {
      groupId: this.groupId!,
      memberId: userId
    };

    return this.groupService.addGroupMember(dto);
  }
    deleteMember(userId: string) {
    this.groupService.deleteGroupMember(this.groupId!, userId).subscribe({
      next: response => {
        console.log("Member deleted successfully:", response);
        this.loadMembersAndUsers(this.groupId!);
        this.messages = [{severity:'success', summary:'Success', detail:'Member deleted successfully'}];
      },
      error: err => console.error("Error deleting member:", err)
    });
    }
}
