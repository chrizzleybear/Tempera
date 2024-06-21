import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { UserEditComponent } from '../user-edit/user-edit.component';
import { DialogModule } from 'primeng/dialog';
import { UserCreateComponent } from '../user-create/user-create.component';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { forkJoin } from 'rxjs';
import {
  DeletionResponseDto,
  GroupManagementControllerService,
  ProjectControllerService,
  SimpleGroupDto,
  SimpleProjectDto,
  UserManagementControllerService,
  Userx,
  UserxDto,
} from '../../../api';
import { DropdownModule } from 'primeng/dropdown';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import RolesEnum = Userx.RolesEnum;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    NgForOf,
    TableModule,
    InputTextModule,
    ButtonModule,
    NgIf,
    UserEditComponent,
    DialogModule,
    UserCreateComponent,
    MessagesModule,
    ToastModule,
    DropdownModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
})
/**
 * @class UsersComponent
 * This component is responsible for managing all users.
 */
export class UsersComponent implements OnInit {

  users: UserxDto[] = [];
  filteredUsers: UserxDto[] = [];
  displayEditDialog: boolean = false;
  selectedUser: any;
  displayCreateDialog: boolean = false;
  displayDeletionPopup: boolean = false;
  deletionResponse: DeletionResponseDto | null;
  deletionResponseMessage: string = '';
  affectedProjects: SimpleProjectDto[] = [];
  transferredProjects: SimpleProjectDto[] = [];
  affectedGroups: SimpleGroupDto[] = [];
  transferredGroups: SimpleGroupDto[] = [];
  availableManagers: { label: string, value: UserxDto } [] | undefined;
  availableUsers: {label: string, value : UserxDto}[] | undefined;
  deleteDisabled: boolean = true;
  userNameToDelete: string = '';
  form: FormGroup;


  constructor(private fb: FormBuilder, private groupService: GroupManagementControllerService, private projectService: ProjectControllerService, private usersService: UserManagementControllerService, private router: Router, private messageService: MessageService) {
    this.deletionResponse = null;
    this.form = this.fb.group({
      row: this.fb.array([]),
    });
  }


  ngOnInit(): void {
    this.loadUsers();
  }

  /**
   * Filters users based on the input value.
   * @param event
   */
  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    if (filterValue) {
      this.filteredUsers = this.users.filter(user =>
        user.username.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.firstName.toLowerCase().includes(filterValue.toLowerCase()) ||
        user.lastName.toLowerCase().includes(filterValue.toLowerCase()),
      );
    } else {
      this.filteredUsers = this.users;
    }
  }

  /**
   * Deletes a user based on the user ID. If deletion is successful, a success message is displayed.
   * If deletion fails, an error message with a hint, why it failed is displayed.
   * @param username The ID of the user to delete.
   */
  deleteSelectedUser(username: string): void {
    this.resetUsers();
    console.log('Delete user with ID: ', username);
    this.deletionRequest(username);
  }


  deletionRequest(username: string): void {
    this.usersService.deleteUser(username).subscribe({
      next: (response) => {
        console.log('User deleted:', response);
        this.evaluateResponse(response, username);
        this.loadUsers();
      },
      error: (error) => {
        console.error('Error deleting user:', error);
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error deleting user' });
      },
    });
  };

  /**
   * Evaluates the response of an API call and displays a success or error message.
   * @param response The response of the API call.
   * @param username The ID of the user that is going to be deleted.
   */
  evaluateResponse(response: DeletionResponseDto, username: string): void {
    this.deletionResponse = response;
    console.log(this.deletionResponse?.responseType);
    this.userNameToDelete = username;

    if (response.responseType === 'SUCCESS') {
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'User deleted' });
      this.resetUsers();
    } else if (response.responseType === 'ERROR') {
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'User could not be deleted' });
    } else if (response.responseType === 'MANAGER') {
      this.availableManagers = this.users.filter(user => {
        const rolesSet = new Set(user.roles);
        return rolesSet.has(RolesEnum.Manager) && user.username !== username;
      }).map(
        manager => ({ label: `${manager.firstName} ${manager.lastName}`, value: manager }),
      );
      this.deletionResponseMessage = 'The user could not be deleted, because they are still managing some Projects.';
      console.log('response', response.affectedProjects);
      this.affectedProjects = response.affectedProjects ?? [];
      this.displayDeletionPopup = true;
    } else if (response.responseType === 'GROUPLEAD') {
      this.availableUsers = this.users.filter(user => user.username !== username).map(
        user => ({ label: `${user.firstName} ${user.lastName}`, value: user }),
      )
      this.deletionResponseMessage = 'The user could not be deleted, because he is a group lead of the following groups:';
      this.affectedGroups = response.affectedGroups ?? [];
      this.displayDeletionPopup = true;
    } else if (response.responseType === 'ADMIN') {

      this.deletionResponseMessage = 'The user could not be deleted, because he is an admin.';
      this.displayDeletionPopup = true;
    }
  }

  transferProject(project: SimpleProjectDto, manager: any) : void {
    project.manager = manager.value.username ?? '';
    this.transferredProjects.push(project);
    this.checkAllProjectsReassigned();
  }

  transferGroup(group: SimpleGroupDto, user: any) : void {
    group.groupLead = user.value.username ?? '';
    this.transferredGroups.push(group);
    this.checkAllGroupsReassigned();
  }



transferAndDeleteProjects(): void {
  const updateProjects$ = this.transferredProjects.map(project =>
    this.projectService.updateProject(project)
  );

  forkJoin(updateProjects$).subscribe({
    next: (responses) => {
      console.log('All projects transferred:', responses);
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'All projects transferred' });

      // Now delete the user
      this.usersService.deleteUser(this.userNameToDelete).subscribe({
        next: (response) => {
          console.log('User deleted:', response);
          this.evaluateResponse(response, this.userNameToDelete);
          this.loadUsers();
          this.displayDeletionPopup = false;
        },
        error: (error) => {
          console.error('Error deleting user:', error);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error deleting user' });
        },
      });
    },
    error: (error) => {
      console.error('Error transferring projects:', error);
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error transferring projects' });
    },
  });
}

transferAndDeleteGroups(): void {
const updateGroups$ = this.transferredGroups.map(group =>
    this.groupService.updateGroup(group)
  );

  forkJoin(updateGroups$).subscribe({
    next: (responses) => {
      console.log('All groups transferred:', responses);
      this.messageService.add({ severity: 'success', summary: 'Success', detail: 'All groups transferred' });

      // Now delete the user
      this.usersService.deleteUser(this.userNameToDelete).subscribe({
        next: (response) => {
          console.log('User deleted:', response);
          this.evaluateResponse(response, this.userNameToDelete);
          this.loadUsers();
          this.displayDeletionPopup = false;
        },
        error: (error) => {
          console.error('Error deleting user:', error);
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error deleting user' });
        },
      });
    },
    error: (error) => {
      console.error('Error transferring groups:', error);
      this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error transferring groups' });
    },
  });
}


  checkAllProjectsReassigned(): void {
    if (this.affectedProjects.filter(project => this.transferredProjects.some(transferredProject => transferredProject.projectId === project.projectId)).length === this.affectedProjects.length) {
      this.deleteDisabled = false;
    }
  }

  checkAllGroupsReassigned(): void {
    if (this.affectedGroups.filter(group => this.transferredGroups.some(transferredGroup => transferredGroup.id === group.id)).length === this.affectedGroups.length) {
      this.deleteDisabled = false;
    }
  }

  resetUsers() {
    this.loadUsers();
    this.deleteDisabled = true;
    this.displayDeletionPopup = false;
    this.affectedProjects = [];
    this.affectedGroups = [];
    this.userNameToDelete = '';
  }

  loadUsers() {
    this.usersService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filteredUsers = users;
    });
  }

  /**
   * Opens the edit user dialog.
   * @param user
   */
  editUser(user: any) {
    this.selectedUser = { ...user };
    this.displayEditDialog = true;
    console.log('detail', user);

  }

  /**
   * Opens the create user dialog.
   */
  createUser() {
    this.displayCreateDialog = true;

  }

  /**
   * Closes the edit or create user dialog to go back to overview.
   */
  returnToUsers() {
    this.loadUsers();
    this.displayEditDialog = false;
    this.displayCreateDialog = false;

  }

  onEditCompleted(success: boolean) {
    if (success) {
      this.returnToUsers();
    }
  }

  jumpToProject(projectId: string) {
    this.router.navigate(['/project', projectId]);
  }

  /**
   * Navigates to the user details page.
   * @param userId The ID of the user to view details of.
   */
  viewUserDetails(userId: string) {
    this.router.navigate(['/user', userId]);
  }

  protected readonly DeletionResponseDto = DeletionResponseDto;
}
