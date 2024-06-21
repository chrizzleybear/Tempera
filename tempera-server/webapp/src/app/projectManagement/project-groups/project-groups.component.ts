import { Component, OnInit } from '@angular/core';
import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { TableModule } from 'primeng/table';
import { MessagesModule } from 'primeng/messages';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import {
  GroupManagementControllerService,
  MinimalGxpDto,
  ProjectControllerService,
  SimpleGroupDto,
} from '../../../api';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  providers: [ConfirmationService, MessageService],
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
    InputTextModule,
    ToastModule,
    ConfirmPopupModule,
    TooltipModule,
  ],
  templateUrl: './project-groups.component.html',
  styleUrl: './project-groups.component.css',
})
/**
 * @class ProjectGroupsComponent
 * This component is used to manage groups assigned to a project.
 */
export class ProjectGroupsComponent implements OnInit {

  activeContributingGroups: SimpleGroupDto[] = [];
  availableGroups: SimpleGroupDto[] = [];
  filteredGroups: SimpleGroupDto[] = [];
  filteredAvailableGroups: SimpleGroupDto[] = [];
  projectId!: string;
  projectName!: string;
  displayAddDialog: boolean = false;
  displayRemoveDialog: boolean = false;
  groupToBeRemoved?: SimpleGroupDto;
  messages: any;

  constructor(
    private projectControllerService: ProjectControllerService,
    private confirmationService: ConfirmationService,
    private route: ActivatedRoute,
    private router: Router,
    private groupControllerService: GroupManagementControllerService,
    private messageService: MessageService) {

  }

  ngOnInit() {
    this.projectId = this.route.snapshot.paramMap.get('id')!;
    //todo: getSimpleProject from ControllerService and check functionality
    this.projectControllerService.getProjectSimpleById(this.projectId).subscribe(project => this.projectName = project.name);
    this.fetchActiveGroupsOfThisProject(this.projectId);
  }

  fetchActiveGroupsOfThisProject(projectId: string) {
    this.projectControllerService.getActiveGroupsByProjectId(projectId).subscribe((activeGroups: SimpleGroupDto[]): void => {
      this.activeContributingGroups = activeGroups;
      this.filteredGroups = activeGroups;
    });
  }

  fetchAllActiveGroups() {
    this.groupControllerService.getAllGroups().subscribe((groups: SimpleGroupDto[]) => {
      this.availableGroups = groups.filter(group => group.isActive).filter((group: { id: string; }) =>
        !this.activeContributingGroups.some(groupP => group.id === groupP.id));
      this.filteredAvailableGroups = this.availableGroups;
    });
  }


  switchToGroup(groupId: string) {
    console.log('Switch to Group with id: ' + groupId);
    this.router.navigate(['group', groupId]);
  }

  // confirm(event: Event, groupId: string) {
  //   this.groupControllerService.getSimpleGroup(groupId).subscribe({
  //     next: (group: SimpleGroupDto) => {
  //       this.groupToBeRemoved = group;
  //     },
  //     error: err => {
  //       this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to remove Group' });
  //       console.error(err);
  //     },
  //   });
  //   this.confirmationService.confirm({
  //     target: event.target ?? undefined,
  //     header: 'Are you sure?',
  //     message: 'All Contributors will be removed...',
  //     icon: 'pi pi-exclamation-circle',
  //     accept: () => {
  //       this.removeGroupFromProject();
  //     },
  //   });
  // }


  removeGroupFromProject(group: SimpleGroupDto) {
    const name = group.name;
    const groupId = group.id;
    this.projectControllerService.removeGroupFromProject(this.projectId.toString(), groupId).subscribe({
      next: () => {
        console.log(groupId, ` removed Group ${groupId}from project with ID: `, this.projectId);
        this.fetchActiveGroupsOfThisProject(this.projectId);
        this.messageService.add({ severity: 'success', summary: 'Success!', detail: `You have removed Group ${name} from this Project` });
        this.displayRemoveDialog = false;
        this.groupToBeRemoved = undefined;
      },
      error: err => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to remove Group' });
        console.error(err);
      },
    });
  }

  addGroupDialog() {
    this.filteredGroups = this.activeContributingGroups;
    this.fetchAllActiveGroups();
    this.displayAddDialog = true;
  }

  addGroupToProject(groupId: string) {
    const minimalGxpDto: MinimalGxpDto = {
      projectId: this.projectId.toString(),
      groupId: groupId,
    };
    this.projectControllerService.addGroupToProject(minimalGxpDto).subscribe(() => {
      this.fetchActiveGroupsOfThisProject(this.projectId);
      this.displayAddDialog = false;
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filteredGroups = filterValue ? this.activeContributingGroups.filter(group =>
      (group.name?.toLowerCase() ?? '').includes(filterValue.toLowerCase())) : this.activeContributingGroups;

    this.filteredAvailableGroups = filterValue ? this.availableGroups.filter(group =>
      (group.name?.toLowerCase() ?? '').includes(filterValue.toLowerCase())) : this.availableGroups;
  }
}

