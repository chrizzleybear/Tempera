import {Component, OnInit} from '@angular/core';
import {Group} from "../models/group.model";
import {GroupService} from "../_services/group.service";
import {Router} from "@angular/router";
import {StorageService} from "../_services/storage.service";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {GroupCreateComponent} from "../groupManagement/group-create/group-create.component";
import {GroupEditComponent} from "../groupManagement/group-edit/group-edit.component";
import {InputTextModule} from "primeng/inputtext";
import {MessagesModule} from "primeng/messages";
import {NgForOf, NgIf} from "@angular/common";
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";

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
    NgForOf
  ],
  templateUrl: './groups-grouplead.component.html',
  styleUrl: './groups-grouplead.component.css'
})
export class GroupsGroupleadComponent implements OnInit{

  groups: Group[] = [];
  filteredGroups: Group[] = [];
  messages: any;
  currentUserId!: string;

  constructor(private groupService: GroupService, private router: Router, private storageService: StorageService) {}

  ngOnInit(): void {
    this.currentUserId = this.storageService.getUser()?.username!;
    this.loadGroups();
  }

  private loadGroups() {
    this.groupService.getGroupByLead(this.currentUserId).subscribe({
      next: (groups) => {
        console.log("Loaded groups:", groups);
        this.groups = groups;
        this.filteredGroups = groups;
      },
      error: (error) => {
        console.error("Error loading groups:", error);
      }
    });
  }

  viewGroupDetails(group: Group) {
    this.router.navigate(['/group', group.id]);
  }

  members(group: Group) {
    this.router.navigate(['/group/members',group.name, group.id]);
  }

  projects(group: Group) {
    this.router.navigate(['/group/projects', group.id]);

  }
}
