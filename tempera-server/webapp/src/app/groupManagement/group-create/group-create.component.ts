import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { UsersService } from '../../_services/users.service';
import { DropdownOptionUser, User } from '../../models/user.model';
import { MessageModule } from 'primeng/message';
import { NgIf } from '@angular/common';
import { GroupManagementControllerService, SimpleGroupDto } from '../../../api';

@Component({
  selector: 'app-group-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    MessageModule,
    NgIf,
  ],
  templateUrl: './group-create.component.html',
  styleUrl: './group-create.component.css',
})
export class GroupCreateComponent implements OnInit {

  groupForm: FormGroup;
  groupLeads: DropdownOptionUser[] = [];
  reactivateForm: FormGroup;
  deactivatedGroups: SimpleGroupDto[] = [];
  deactivatedGroupOptions: { label: string, value: string } [] | undefined;
  deactivatedGroupsExist: boolean = false;

  @Output() createCompleted = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private groupService: GroupManagementControllerService, private usersService: UsersService) {
    this.groupForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      groupLead: [null, [Validators.required]],
    });
    this.reactivateForm = this.fb.group({
      group: ['', [Validators.required]],
    });
  }

  @Input({ required: true }) groupCreationEvent!: EventEmitter<void>;

  ngOnInit() {
    this.groupCreationEvent.subscribe(() => {
      this.fetchDeactivatedGroups();
      this.fetchGroupLeads();
    });
  }


  fetchDeactivatedGroups() {
    this.groupService.getAllGroups().subscribe({
      next: (groups) => {
        this.deactivatedGroups = groups.filter(group => !group.isActive);
        this.deactivatedGroupOptions = this.deactivatedGroups.map(group => ({ label: group.name, value: group.id }));
        this.deactivatedGroupsExist = this.deactivatedGroupOptions.length > 0;
        console.log('Deactivated groups:', this.deactivatedGroups);
      },
      error: (error) => console.error('Error loading groups:', error),
    });
  }

  fetchGroupLeads() {
    this.usersService.getAllUsers().subscribe({
      next: (users: User[]) => {
        console.log('Loaded users:', users);
        this.groupLeads = users.map(user => ({ label: `${user.firstName} ${user.lastName}`, value: user }));
        console.log('User dropdown options:', this.groupLeads);
      },
      error: (error) => console.error('Error loading users:', error),
    });
  }

  reactivateGroup() {
    if (this.reactivateForm.valid) {
      const groupId = this.reactivateForm.value.group.value;

      this.groupService.reactivateGroup(groupId).subscribe({
        next: (response) => {
          console.log('Group reactivated:', response);
          this.reactivateForm.reset();
          this.createCompleted.emit(true);
        },
        error: (error) => console.error('Error reactivating group:', error),
      });
    }
  }


  onSubmit() {
    if (this.groupForm.valid) {
      const dto: SimpleGroupDto = {
        name: this.groupForm.value.name,
        description: this.groupForm.value.description,
        groupLead: this.groupForm.value.groupLead.value.username,
        id: '',
        isActive: true,
      };
      this.groupService.createGroup(dto,
      ).subscribe({
        next: (response) => {
          console.log('Group created:', response);
          this.groupForm.reset();
          this.createCompleted.emit(true);
        },
        error: (error) => console.error('Error creating group:', error),
      });
    }
  }
}
