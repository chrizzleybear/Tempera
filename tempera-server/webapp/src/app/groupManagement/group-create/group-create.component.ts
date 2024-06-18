import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {DropdownModule} from 'primeng/dropdown';
import {GroupService} from '../../_services/group.service';
import {UsersService} from '../../_services/users.service';
import {DropdownOptionUser, User} from '../../models/user.model';
import {GroupCreateDTO} from "../../models/groupDtos";
import {MessageModule} from "primeng/message";
import {NgIf} from "@angular/common";
import {MessageService} from "primeng/api";

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
  styleUrl: './group-create.component.css'
})
export class GroupCreateComponent implements OnInit{

  groupForm: FormGroup;
  groupLeads: DropdownOptionUser[] = [];

  @Output() createCompleted = new EventEmitter<boolean>();

    constructor(
      private fb: FormBuilder,
      private groupService: GroupService,
      private usersService: UsersService,
      private messageService: MessageService) {
        this.groupForm = this.fb.group({
        name: ['', [Validators.required, Validators.minLength(3)]],
        description: ['', [Validators.required]],
        groupLead: [null, [Validators.required]]
        });
    }

    ngOnInit() {
        this.fetchGroupLeads();
    }

    fetchGroupLeads() {
      this.usersService.getAllUsers().subscribe({
        next: (users: User[]) => {
          console.log('Loaded users:', users);
          this.groupLeads = users.map(user => ({ label: `${user.firstName} ${user.lastName}`, value: user }));
          console.log('User dropdown options:', this.groupLeads);
        },
        error: (error) => console.error('Error loading users:', error)
      });
    }

    onSubmit() {
        if (this.groupForm.valid) {
          const dto: GroupCreateDTO = {
            name: this.groupForm.value.name,
            description: this.groupForm.value.description,
            groupLead: this.groupForm.value.groupLead.value.username
          }
            this.groupService.createGroup(dto
            ).subscribe({
                next: (response) => {
                  this.messageService.add({severity:'success', summary:'Success', detail:'Group created successfully'});
                    console.log('Group created:', response);
                    this.groupForm.reset();
                    this.createCompleted.emit(true);
                },
                error: (error) => {
                  this.messageService.add({severity:'error', summary:'Error', detail:'Error creating group'});
                  console.error('Error creating group:', error)
                }
            });
        }
    }
}
