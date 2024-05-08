import {Component, OnInit} from '@angular/core';
import {ProjectService} from "../../_services/project.service";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {Group} from "../../models/group.model";
import {ActivatedRoute} from "@angular/router";
import {TableModule} from "primeng/table";
import {GroupService} from "../../_services/group.service";
import {MessagesModule} from "primeng/messages";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {GroupAssignmentDTO} from "../../models/projectDtos";

@Component({
  selector: 'app-project-groups',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf,
    NgForOf,
    TableModule,
    MessagesModule,
    ButtonModule,
    DialogModule
  ],
  templateUrl: './project-groups.component.html',
  styleUrl: './project-groups.component.css'
})
export class ProjectGroupsComponent implements OnInit{

  groups: Group[] = [];
  allGroups: Group[] = [];
  projectId!: number;
  displayAddDialog: boolean = false;
  messages: any;
  constructor(private projectService: ProjectService, private route: ActivatedRoute, private groupService: GroupService) {

  }

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id')!);
    this.fetchGroups(this.projectId);
  }

  fetchGroups(projectId: number) {
    this.projectService.getGroups(projectId).subscribe((groups: Group[]) => {
      this.groups = groups;
    });
  }

  fetchAllGroups() {
    this.groupService.getAllGroups().subscribe((groups: Group[]) => {
      this.allGroups = groups.filter((group: { id: string; }) =>
        !this.groups.some(groupP => group.id === groupP.id));
    });
  }

  addGroupDialog() {
    this.fetchAllGroups();
    this.displayAddDialog = true;
  }

  addGroupToProject(groupId: number) {
    const dto: GroupAssignmentDTO = {
      projectId: this.projectId!,
      groupId: groupId
    }
    this.projectService.addGroupToProject(dto).subscribe(() => {
      this.fetchGroups(this.projectId);
      this.displayAddDialog = false;
    });
  }

  deleteGroupFromProject(groupId: number) {
    const dto: GroupAssignmentDTO = {
      projectId: Number(this.projectId),
      groupId: groupId
    }
    this.projectService.deleteGroupFromProject(dto).subscribe(() => {
      console.log(groupId, " deleted from project with ID: ", this.projectId);
      this.fetchGroups(this.projectId);
    });
  }
}

