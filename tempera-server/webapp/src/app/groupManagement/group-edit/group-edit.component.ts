import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {DropdownOptionUser, User} from "../../models/user.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GroupService} from "../../_services/group.service";
import {Group} from "../../models/group.model";
import {UsersService} from "../../_services/users.service";
import {DropdownModule} from "primeng/dropdown";
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";
import {NgIf} from "@angular/common";
import {MessageModule} from "primeng/message";
import {GroupUpdateDTO} from "../../models/groupDtos";
import { SimpleGroupDto } from '../../../api';

@Component({
  selector: 'app-group-edit',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
    NgIf,
    MessageModule,
  ],
  templateUrl: './group-edit.component.html',
  styleUrl: './group-edit.component.css'
})
export class GroupEditComponent implements OnInit, OnChanges{

  groupForm: FormGroup;
  groupLeads: any[] = [];

  @Input({required: true}) group!: SimpleGroupDto;
  @Output() editComplete = new EventEmitter<unknown>();

  constructor(
    private fb: FormBuilder,
    private groupService: GroupService,
    private usersService: UsersService
  ) {
    this.groupForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      groupLead: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    this.fetchGroupLeads();
  }

  ngOnChanges(change: SimpleChanges) {
    this.groupForm.reset();
    this.populateForm();

  }

  fetchGroupLeads() {
    this.usersService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.groupLeads = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`,
          value: user.username
        }));
        this.populateForm();
      },
      error: (error) => {
        console.error('Error loading users:', error);
      }
    });
  }

  private populateForm() {
    this.groupForm.patchValue({
      name: this.group.name,
      description: this.group.description,
      groupLead: this.groupLeads.find(lead => lead.value === this.group.groupLead)
    });
    console.log('Populated form:', this.groupForm.value);
  }

  onSubmit() {
    if (this.groupForm.valid) {
      const dto: GroupUpdateDTO = {
        id: Number(this.group.id),
        name: this.groupForm.value.name,
        description: this.groupForm.value.description,
        groupLead: this.groupForm.value.groupLead.value
      };
      this.groupService.updateGroup(dto).subscribe({
        next: (response) => {
          console.log('Group updated:', response);
          this.editComplete.emit(response);
        },
        error: (error) => {
          console.error('Error updating group:', error);
        }
      });
    }
  }
}
