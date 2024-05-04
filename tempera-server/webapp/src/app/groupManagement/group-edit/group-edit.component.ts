import {Component, EventEmitter, Input, Output, SimpleChanges} from '@angular/core';
import {User} from "../../models/user.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {GroupService} from "../../_services/group.service";
import {Group} from "../../models/group.model";

@Component({
  selector: 'app-group-edit',
  standalone: true,
  imports: [],
  templateUrl: './group-edit.component.html',
  styleUrl: './group-edit.component.css'
})
export class GroupEditComponent {

  groupForm: FormGroup;
  group!: Group;
  @Input({ required: true }) user!: User;
  @Output() editCompleted = new EventEmitter<boolean>();

  constructor(private fb: FormBuilder, private groupService: GroupService) {
    this.groupForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      groupLead: [null, [Validators.required]] // Assuming groupLead is required
    });
  }

  ngOnInit() {
    if (this.group) {
      this.populateForm();
    } else {
      console.error('Group data is not provided!');
    }
  }

  private populateForm() {
    this.groupForm.patchValue({
      name: this.group.name,
      description: this.group.description,
      groupLead: this.group.groupLead
    });
  }
}
