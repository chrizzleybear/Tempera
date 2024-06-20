import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {StorageService} from "../_services/storage.service";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {GroupCreateComponent} from "../groupManagement/group-create/group-create.component";
import {GroupEditComponent} from "../groupManagement/group-edit/group-edit.component";
import {InputTextModule} from "primeng/inputtext";
import {MessagesModule} from "primeng/messages";
import {NgForOf, NgIf} from "@angular/common";
import {MessageService, SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";
import { GroupManagementControllerService, SimpleGroupDto } from '../../api';
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-groups-grouplead',
  standalone: true,
  imports: [
    ButtonModule,
    DialogModule,
    GroupCreateComponent,
    GroupEditComponent,
    InputTextModule,
    MessagesModule,
    NgIf,
    SharedModule,
    TableModule,
    CardModule,
    NgForOf,
    ToastModule
  ],
  templateUrl: './groups-grouplead.component.html',
  styleUrl: './groups-grouplead.component.css'
})
/**
 * @class GroupsGroupleadComponent
 * This component is used to display all groups of a group lead.
 */
export class GroupsGroupleadComponent implements OnInit{

  groups: SimpleGroupDto[] = [];
  filteredGroups: SimpleGroupDto[] = [];
  currentUserId!: string;

  constructor(private groupService: GroupManagementControllerService, private router: Router, private storageService: StorageService, private messageService: MessageService) {}

  ngOnInit(): void {
    this.currentUserId = this.storageService.getUser()?.username!;
    this.loadGroups();
  }

  private loadGroups() {
    this.groupService.getGroupsByGroupLead(this.currentUserId).subscribe({
      next: (groups) => {
        console.log("Loaded groups:", groups);
        this.groups = groups.filter(g => g.isActive);
        this.filteredGroups = this.groups;
      },
      error: (error) => {
        this.messageService.add({severity:'error', summary:'Error', detail:'Failed to load groups.'});
        console.error("Error loading groups:", error);
      }
    });
  }

  viewGroupDetails(group: SimpleGroupDto) {
    this.router.navigate(['/group', group.id]);
  }

  members(group: SimpleGroupDto) {
    this.router.navigate(['/group/members',group.name, group.id]);
  }

  projects(group: SimpleGroupDto) {
    this.router.navigate(['/group/projects', group.name, group.id]);
  }
}
