import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

import {
  GroupDetailsDto,
  GroupManagementControllerService, ProjectControllerService,
  SimpleProjectDto,
  SimpleUserDto,
} from '../../../api';
import { PanelModule } from 'primeng/panel';

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
  ],
  templateUrl: './group-details.component.html',
  styleUrl: './group-details.component.css'
})
export class GroupDetailsComponent implements OnInit{

  group: GroupDetailsDto | undefined;
  associatedProjects: SimpleProjectDto[] = [];
  groupId: string | null | undefined;
  members: SimpleUserDto[] = [];

  constructor(
    private route: ActivatedRoute,
    private groupService: GroupManagementControllerService,
    private projectService: ProjectControllerService
  ) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.paramMap.get('id');
    if (this.groupId) {
      this.fetchGroupDetails(this.groupId);
      this.fetchProjects(this.groupId);
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
