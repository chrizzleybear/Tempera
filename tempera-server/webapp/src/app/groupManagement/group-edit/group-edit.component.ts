import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {User} from "../../models/user.model";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {GroupService} from "../../_services/group.service";
import {Group} from "../../models/group.model";
import {UsersService} from "../../_services/users.service";
import {DropdownModule} from "primeng/dropdown";
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";
import {NgIf} from "@angular/common";
import {MessageModule} from "primeng/message";

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

  @Input() group!: Group;
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

  ngOnChanges() {
    this.groupForm.reset();
    this.populateForm();

  }

  fetchGroupLeads() {
    this.usersService.getAllUsers().subscribe({
      next: (users: User[]) => {
        this.groupLeads = users.map(user => ({
          label: `${user.firstName} ${user.lastName}`,
          value: user
        }));
        if (this.group) {
          this.populateForm();
        }
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
      groupLead: this.groupLeads.find(lead => lead.value.username === this.group.groupLead.id)
    });
  }

  onSubmit() {
    if (this.groupForm.valid) {
      this.groupService.updateGroup(
        this.group.id,
        this.groupForm.value.name,
        this.groupForm.value.description,
        this.groupForm.value.groupLead.value.username
      ).subscribe({
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
