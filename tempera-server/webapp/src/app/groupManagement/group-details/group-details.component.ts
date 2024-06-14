import {Component, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

import {
  GroupDetailsDto,
  GroupManagementControllerService, ProjectControllerService, SimpleGroupDto,
  SimpleProjectDto,
  SimpleUserDto,
} from '../../../api';
import { PanelModule } from 'primeng/panel';
import { BadgeModule } from 'primeng/badge';
import { DockModule } from 'primeng/dock';
import { Group } from '../../models/group.model';
import { DialogModule } from 'primeng/dialog';
import { GroupEditComponent } from '../group-edit/group-edit.component';
import { DividerModule } from 'primeng/divider';

@Component({
  selector: 'app-group-details',
  standalone: true,
  imports: [
    CardModule,
    NgIf,
    NgForOf,
    TableModule,
    ButtonModule,
    PanelModule,
    BadgeModule,
    DockModule,
    DialogModule,
    GroupEditComponent,
    DividerModule,
  ],
  templateUrl: './group-details.component.html',
  styleUrl: './group-details.component.css'
})
export class GroupDetailsComponent implements OnInit{

  group: GroupDetailsDto | undefined;
  simpleEditGroup: SimpleGroupDto | undefined;
  associatedProjects: SimpleProjectDto[] = [];
  groupId: string | null | undefined;
  members: SimpleUserDto[] = [];
  displayEditDialog: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private groupService: GroupManagementControllerService,
    private projectService: ProjectControllerService
  ) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.paramMap.get('id');
    if (this.groupId) {
      this.fetchGroupDetails(this.groupId);
    }
  }

  fetchGroupDetails(id: string) {
    this.groupService.getExtendedGroup(id).subscribe({
      next: (data) => {
        this.group = data.groupDetailsDto;
        this.members = Array.from(data.members);
        this.associatedProjects = data.activeProjects;
        console.log('Group : ', this.group);
        console.log('Group members: ', this.members);
        console.log('Group projects: ', this.associatedProjects);
      },
      error: (error) => {
        console.error('Failed to load group details:', error);
      },
    });
  }

  editGroup() {
    console.log("Edit group");
    this.fetchSimpleGroupDto(this.group!.id);
    this.displayEditDialog = true;
  }
  onEditCompleted($event: any) {
    this.displayEditDialog = false;
    this.fetchGroupDetails(this.group!.id);
  }

  fetchSimpleGroupDto(id: string) {
    this.groupService.getSimpleGroup(id).subscribe({
      next: (data) => {
        this.simpleEditGroup = data;
        console.log('SimpleGroupDto : ', this.group);
      },
      error: (error) => {
        console.error('Failed to load simpleGroup:', error);
      },
    });
  }

  switchToProject(projectId: string){
    console.log("Switch to Project with id: " + projectId);
    this.router.navigate(['/project/groups/', projectId]);
  }

  switchToProjectManagement(){
    console.log("Switch to Project Management");
    this.router.navigate(['/projects']);
  }

  switchToGroupMembers(){
    console.log("Switch to Group Members");
    this.router.navigate(['/group/members',this.group?.name, this.group?.id]);
  }

  fetchProjects(id: string) {
    this.projectService.getProjectsByGroupId(id).subscribe({
      next: (data) => {
        this.associatedProjects = data;
        console.log('Group projects: ', this.associatedProjects);
      },
      error: (error) => {
        console.error('Failed to load group projects:', error);
      },
    });
    }
}
