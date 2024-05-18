import {Component, OnInit} from '@angular/core';
import {Group} from "../../models/group.model";
import {ActivatedRoute} from "@angular/router";
import {GroupService} from "../../_services/group.service";
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-group-details',
  standalone: true,
  imports: [
    CardModule,
    NgIf,
    NgForOf,
    TableModule,
    ButtonModule
  ],
  templateUrl: './group-details.component.html',
  styleUrl: './group-details.component.css'
})
export class GroupDetailsComponent implements OnInit{

  group: Group | undefined;
  groupId: string | null | undefined;

  constructor(
    private route: ActivatedRoute,
    private groupService: GroupService,
  ) {
  }

  ngOnInit() {
    this.groupId = this.route.snapshot.paramMap.get('id');
    if (this.groupId) {
      this.fetchGroupDetails(this.groupId);
    }
  }

  fetchGroupDetails(id: string) {
    this.groupService.getGroupById(Number(id)).subscribe({
      next: (data) => {
        this.group = data;
        console.log('Group details: ', this.group);
      },
      error: (error) => {
        console.error('Failed to load group details:', error);
      },
    });
  }
}
