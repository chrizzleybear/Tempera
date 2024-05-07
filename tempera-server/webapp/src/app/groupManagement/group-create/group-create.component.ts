import {Component, EventEmitter, Output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {InputTextModule} from 'primeng/inputtext';
import {DropdownModule} from 'primeng/dropdown';
import {GroupService} from '../../_services/group.service';
import {UsersService} from '../../_services/users.service';
import {User} from '../../models/user.model';
import {GroupCreateDTO} from "../../models/groupDtos";

@Component({
  selector: 'app-group-create',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    DropdownModule,
  ],
  templateUrl: './group-create.component.html',
  styleUrl: './group-create.component.css'
})
export class GroupCreateComponent {

  groupForm: FormGroup;
  groupLeads: any[] | undefined;

  @Output() createCompleted = new EventEmitter<boolean>();

    constructor(private fb: FormBuilder, private groupService: GroupService, private usersService: UsersService) {
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
                    console.log('Group created:', response);
                    this.createCompleted.emit(true);
                },
                error: (error) => console.error('Error creating group:', error)
            });
        }
    }


}
