import {Component, OnInit} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {TableModule} from "primeng/table";
import {MessagesModule} from "primeng/messages";
import {ButtonModule} from "primeng/button";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {
  GroupManagementControllerService,
  MinimalGxpDto,
  ProjectControllerService,
  SimpleGroupDto,
} from '../../../api';

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
    DialogModule,
    InputTextModule
  ],
  templateUrl: './project-groups.component.html',
  styleUrl: './project-groups.component.css'
})
/**
 * @class ProjectGroupsComponent
 * This component is used to manage groups assigned to a project.
 */
export class ProjectGroupsComponent implements OnInit{

  activeGroups: SimpleGroupDto[] = [];
  availableGroups: SimpleGroupDto[] = [];
  filteredGroups: SimpleGroupDto[] = [];
  filteredAvailableGroups: SimpleGroupDto[] = [];
  projectId!: number;
  projectName!: string;
  displayAddDialog: boolean = false;
  messages: any;
  constructor(private projectControllerService: ProjectControllerService, private route: ActivatedRoute, private groupControllerService: GroupManagementControllerService) {

  }

  ngOnInit() {
    this.projectId = Number(this.route.snapshot.paramMap.get('id')!);
    //todo: getSimpleProject from ControllerService and check functionality
    this.projectName = this.projectControllerService.get
    this.fetchActiveGroups(this.projectId);
  }

  fetchActiveGroups(projectId: number) {
    // todo: where to parse projectId to string?
    this.projectControllerService.getActiveGroupsByProjectId(projectId.toString()).subscribe((activeGroups: SimpleGroupDto[]) : void => {
      this.activeGroups = activeGroups;
      //todo: what is this.filteredGroups?
      this.filteredGroups = activeGroups;
    });
  }

  fetchAllActiveGroups() {
    this.groupControllerService.getAllActiveGroups().subscribe((groups: SimpleGroupDto[]) => {
      this.availableGroups = groups.filter((group: { id: string; }) =>
        !this.activeGroups.some(groupP => group.id === groupP.id));
      this.filteredAvailableGroups = this.availableGroups;
    });
  }

  addGroupDialog() {
    this.filteredGroups = this.activeGroups;
    this.fetchAllActiveGroups();
    this.displayAddDialog = true;
  }

  addGroupToProject(groupId: string) {
    const minimalGxpDto: MinimalGxpDto = {
      projectId: this.projectId.toString(),
      groupId: groupId
    }
    this.projectControllerService.addGroupToProject(minimalGxpDto).subscribe(() => {
      this.fetchActiveGroups(this.projectId);
      this.displayAddDialog = false;
    });
  }

  deleteGroupFromProject(groupId: number) {
    this.projectControllerService.removeGroupFromProject(this.projectId.toString(), groupId.toString()).subscribe(() => {
      console.log(groupId, ` removed Group ${groupId}from project with ID: `, this.projectId);
      this.fetchActiveGroups(this.projectId);
    });
  }
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filteredGroups = filterValue ? this.activeGroups.filter(group =>
      (group.name?.toLowerCase() ?? '').includes(filterValue.toLowerCase())) : this.activeGroups;

    this.filteredAvailableGroups = filterValue ? this.availableGroups.filter(group =>
      (group.name?.toLowerCase() ?? '').includes(filterValue.toLowerCase())) : this.availableGroups;
  }
}

