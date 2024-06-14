import { Component, OnInit } from '@angular/core';
import { Group } from "../../models/group.model";
import { GroupService } from '../../_services/group.service';
import { MessagesModule } from "primeng/messages";
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { InputTextModule } from "primeng/inputtext";
import { NgIf } from "@angular/common";
import { DialogModule } from "primeng/dialog";
import { Router } from "@angular/router";
import {GroupCreateComponent} from "../group-create/group-create.component";
import {GroupEditComponent} from "../group-edit/group-edit.component";
import { GroupManagementControllerService, SimpleGroupDto } from '../../../api';
@Component({
  selector: 'app-groups',
  standalone: true,
  imports: [
    MessagesModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    NgIf,
    DialogModule,
    GroupCreateComponent,
    GroupEditComponent,

  ],
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
/**
 * @class GroupsComponent
 * This component is responsible for managing and displaying all groups.
 */
export class GroupsComponent implements OnInit {
  groups: SimpleGroupDto[] = [];
  filteredGroups: SimpleGroupDto[] = [];
  displayCreateDialog: boolean = false;
  displayEditDialog: boolean = false;
  selectedGroup: Group | undefined;
  messages: any;

  constructor(private groupService: GroupManagementControllerService, private router: Router) {}

  ngOnInit(): void {
    this.loadGroups();
  }

  private loadGroups() {
    this.groupService.getAllGroups().subscribe({
      next: (groups) => {
        console.log("Loaded groups:", groups);
        this.groups = groups.filter(g => g.isActive);
        this.filteredGroups = this.groups;
      },
      error: (error) => {
        console.error("Error loading groups:", error);
      }
    });
  }
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filteredGroups = filterValue ? this.groups.filter(group =>
      group.name.toLowerCase().includes(filterValue.toLowerCase())) : this.groups;
  }

  createGroup() {
    this.displayCreateDialog = true;
  }

  deleteGroup(groupId: string) {
    this.groupService.deleteGroup(groupId).subscribe({
      next: (response) => {
        this.loadGroups();
        this.messages = [{severity:'success', summary:'Success', detail:'Group deleted successfully'}];
      },
      error: (error) => {
        this.messages = [{severity:'error', summary:'Error', detail:'Error deleting group'}];
      }
    });
  }

  viewGroupDetails(group: Group) {
    console.log("View group details:", group);
    this.router.navigate(['/group', group.id]);
  }

  editGroup(group: Group) {
    console.log("Edit group:", group);
    this.selectedGroup = group;
    this.displayEditDialog = true;
  }

  onCreateCompleted($event: any) {
    this.displayCreateDialog = false;
    this.loadGroups();
  }

  onEditCompleted($event: any) {
    this.displayEditDialog = false;
    this.loadGroups();
  }

  members(group: Group) {
    this.router.navigate(['/group/members',group.name, group.id]);
  }
}
