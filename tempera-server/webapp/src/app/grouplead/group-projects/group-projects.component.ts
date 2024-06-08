import { Component, OnInit } from '@angular/core';
import { Project } from '../../models/project.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { SharedModule } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';
import { MessagesModule } from 'primeng/messages';
import { from } from 'rxjs';
import { concatMap, toArray } from 'rxjs/operators';
import {
  ContributorAssignmentDto,
  GroupManagementControllerService,
  ProjectControllerService,
  SimpleProjectDto,
  SimpleUserDto, UserxDto,
} from '../../../api';

@Component({
  selector: 'app-group-projects',
  standalone: true,
  imports: [
    ButtonModule,
    SharedModule,
    TableModule,
    DialogModule,
    InputTextModule,
    MessageModule,
    NgIf,
    MessagesModule
  ],
  templateUrl: './group-projects.component.html',
  styleUrls: ['./group-projects.component.css']
})
/**
 * @class GroupProjectsComponent
 * This component is responsible for managing projects of groups.
 */
export class GroupProjectsComponent implements OnInit {
  projects: SimpleProjectDto[] = [];
  messages: any;
  displayAddMemberDialog: boolean = false;
  displayDeleteMemberDialog: boolean = false;
  selectedProject: SimpleProjectDto | undefined;
  members: SimpleUserDto[] = [];
  contributors: SimpleUserDto[] = [];
  availableProjectContributors: SimpleUserDto[] = [];
  filteredMembers: SimpleUserDto[] = [];
  selectedMembers: SimpleUserDto[] = [];
  groupId!: string ;
  groupName: string | null | undefined;
  constructor(private projectService: ProjectControllerService, private groupService: GroupManagementControllerService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.groupId = params.get('id')!;
      this.groupName = params.get('name')!;
    });
    if (this.groupId) {
      this.loadProjects(this.groupId);
      this.loadGroupMembers(this.groupId);
    }
  }

  private loadProjects(groupId: string) {
    this.projectService.getProjectsByGroupId(groupId).subscribe({
      next: (projects) => {
        console.log('Loaded projects:', groupId);
        console.log('Project of group projects:', projects);
        this.projects = projects;
      },
      error: (error) => {
        console.error('Error loading projects:', error);
      }
    });
  }
  private loadProjectContributors(projectId: string) {
    this.projectService.getContributors(this.groupId, projectId).subscribe({
      next: (contributors) => {
        this.contributors = contributors;
        this.availableProjectContributors = this.members.filter(member => !contributors.some(contributor => contributor.username === member.username));
        this.filteredMembers = [...this.availableProjectContributors];
      },
      error: (error) => {
        console.error('Error loading projects:', error);
      }
    });
    }

  private loadGroupMembers(groupId: string) {
    this.groupService.getMembers(groupId).subscribe({
      next: (members) => {
        console.log('Loaded group members:', members);
        this.members = members;
      },
      error: (error) => {
        console.error('Error loading group members:', error);
      }
    });
  }

  viewProjectDetails(project: Project) {
    console.log('View project details:', project);
    console.log('Project ID:', project.projectId);
    this.router.navigate(['/project', project.projectId]);
  }

  addContributorsToProjectDialog(project: SimpleProjectDto) {
    this.displayAddMemberDialog = true;
    this.selectedMembers = [];
    this.selectedProject = project;
    this.loadProjectContributors(project.projectId);
  }

  private addContributorToProject(memberId: string) {
    const dto: ContributorAssignmentDto = {
      projectId: this.selectedProject!.projectId,
      groupId: this.groupId!,
      contributorId: memberId
    };
    return this.projectService.addContributor(dto);
  }

  /**
   * Add selected members to the project.
   * Pipe the selected members to addContributorToProject method and subscribe to the responses.
   */
  addContributorsToProject() {
    from(this.selectedMembers.map(member => member.username))
      .pipe(
        concatMap(memberId => this.addContributorToProject(memberId!)),
        toArray()
      )
      .subscribe({
        next: responses => {
          console.log('All members added successfully:', responses);
          this.loadProjects(this.groupId!);
          this.messages = [{ severity: 'success', summary: 'Success', detail: 'Contributors added successfully' }];
        },
        error: err => console.error('Error adding member:', err)
      });
    this.displayAddMemberDialog = false;
  }

  applyFilterUsers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredMembers = this.availableProjectContributors.filter(user =>
        user.username!.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName!.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName!.toLowerCase().includes(filterValue.toLowerCase())
      );
    } else {
      this.filteredMembers = this.availableProjectContributors;
    }
  }

  deleteContributorDialog(project: SimpleProjectDto) {
    this.displayDeleteMemberDialog = true;
    this.selectedProject = project;
    this.loadProjectContributors(project.projectId);
  }

  deleteContributorsFromProject() {
    from(this.selectedMembers.map(member => member.username))
      .pipe(
        concatMap(memberId => this.deleteContributorFromProject(this.selectedProject!.projectId, memberId!)),
        toArray()
      )
      .subscribe({
        next: responses => {
          console.log('All members removed successfully:', responses);
          this.loadProjects(this.groupId!);
          this.resetMembers();
          this.messages = [{ severity: 'success', summary: 'Success', detail: 'Contributors removed successfully' }];
        },
        error: err => console.error('Error removing member:', err)
      });
    this.displayDeleteMemberDialog = false;
  }

  private deleteContributorFromProject(projectId: string, memberId: string) {
    return this.projectService.removeContributor(projectId, this.groupId, memberId);
  }

  backToGroups() {
    this.router.navigate(['/myGroups']);
  }

  resetMembers() {
    this.selectedMembers = [];
    this.availableProjectContributors = [];
    this.filteredMembers = [];
    this.selectedProject = undefined;
  }
}
