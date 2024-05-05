import {Component, Input} from '@angular/core';
import {Project} from "../../models/project.model";
import {ProjectService} from "../../_services/project.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ButtonModule} from "primeng/button";
import {SharedModule} from "primeng/api";
import {TableModule} from "primeng/table";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {User} from "../../models/user.model";
import {GroupService} from "../../_services/group.service";

@Component({
  selector: 'app-group-projects',
  standalone: true,
  imports: [
    ButtonModule,
    SharedModule,
    TableModule,
    DialogModule,
    InputTextModule
  ],
  templateUrl: './group-projects.component.html',
  styleUrl: './group-projects.component.css'
})
export class GroupProjectsComponent {

  projects: Project[] = [];
  messages: any;
  displayAddMemberDialog: boolean = false;
  selectedProject!: Project;
  members: User[] = [];
  avaiableProjectContribuiters: User[] = [];
  filteredMembers: User[] = [];
  selcetedMembers: User[] = [];

  @Input() groupId!: string | null;

  constructor(private projectService: ProjectService, private groupService: GroupService, private route: ActivatedRoute, private router: Router) {

  }
  ngOnInit(): void {
    this.groupId = this.route.snapshot.paramMap.get('id');
    if (this.groupId) {
      this.loadProjects(this.groupId);
      this.loadGroupMembers(this.groupId);
    }
  }

  private loadProjects(groupId: string) {
    this.projectService.getProjectsOfGroup(groupId).subscribe({
      next: (projects) => {
        console.log("Loaded projects:", projects);
        this.projects = projects;
      },
      error: (error) => {
        console.error("Error loading projects:", error);
      }
    });
  }

  private loadGroupMembers(groupId: string) {
    this.groupService.getGroupMembers(groupId).subscribe({
      next: (members) => {
        console.log("Loaded group members:", members);
        this.members = members;
      },
      error: (error) => {
        console.error("Error loading group members:", error);
      }
    });
  }

  viewProjectDetails(project: Project) {
    console.log("View project details:", project);
    console.log("Project ID:", project.id);
    this.router.navigate(['/project', project.id]);
  }

  addContributorsToProjectDialog(project: Project) {
    console.log("Add contributors to project:", project);
    this.displayAddMemberDialog = true;
    this.selectedProject = project;
    //this.avaiableProjectContribuiters = this.members;
    this.avaiableProjectContribuiters = this.members.filter((member: { username: string; }) =>
     !project.contributors!.some(projectMember => projectMember.username === member.username)
    );
    this.filteredMembers = [...this.avaiableProjectContribuiters];
    console.log("Avaiable project contributors:", this.avaiableProjectContribuiters);

  }

  addContributorToProject(memberId: string) {
    this.projectService.addMemberToProject(this.selectedProject.id.toString(), memberId).subscribe({
      next: (response) => {
        console.log("Member added to project:", response);
      },
      error: (error) => {
        console.error("Error adding member to project:", error);
      }
    });
    }

  addContributorsToProject() {
    this.selcetedMembers.forEach(member => {
      this.addContributorToProject(member.username);
    }
    );
    this.displayAddMemberDialog = false;
    this.selcetedMembers = [];
  }

  applyFilterUsers(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredMembers = this.avaiableProjectContribuiters.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredMembers = this.avaiableProjectContribuiters;
    }
  }
  loadMembers(groupId: string) {
    this.groupService.getGroupMembers(groupId).subscribe({
      next: members => {
        this.members = members;
        this.filteredMembers = [...members];
      },
      error: error => {
        console.error("Error loading members:", error);
      }
    });
  }
}
