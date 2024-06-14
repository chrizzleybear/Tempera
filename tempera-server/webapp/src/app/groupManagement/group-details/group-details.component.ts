import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

import {
  GroupDetailsDto,
  GroupManagementControllerService,
  SimpleProjectDto,
  SimpleUserDto,
} from '../../../api';

@Component({
  selector: 'app-group-details',
  standalone: true,
  imports: [
    CardModule,
    NgIf,
    NgForOf,
    TableModule,
    ButtonModule
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
  ) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.paramMap.get('id');
    if (this.groupId) {
      this.fetchGroupDetails(this.groupId);
      this.fetchGroupMembers(this.groupId);
    }
  }

  fetchGroupDetails(id: string) {
    this.groupService.getExtendedGroup(id).subscribe({
      next: (data) => {
        this.group = data.groupDetailsDto;
        this.associatedProjects = data.activeProjects;
        console.log('Group details: ', this.group);
      },
      error: (error) => {
        console.error('Failed to load group details:', error);
      },
    });
  }
  fetchGroupMembers(id: string) {
    this.groupService.getMembers(id).subscribe({
      next: (data) => {
        this.members = data;
        console.log('Group members: ', this.members);
      },
      error: (error) => {
        console.error('Failed to load group members:', error);
      },
    });
  }
}
