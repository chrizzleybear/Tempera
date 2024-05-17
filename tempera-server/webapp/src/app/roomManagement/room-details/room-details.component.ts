import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RoomService} from "../../_services/room.service";
import {Room} from "../../models/room.model";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {Threshold} from "../../models/threshold.model";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";

@Component({
  selector: 'app-room-details',
  standalone: true,
  imports: [
    TableModule,
    CardModule,
    NgForOf,
    NgIf,
    ButtonModule,
    RippleModule,
    DialogModule,
    FormsModule,
    InputTextModule
  ],
  templateUrl: './room-details.component.html',
  styleUrl: './room-details.component.css'
})
export class RoomDetailsComponent {
  private roomId!: string;
  room: Room | undefined;
  displayEditThresholdDialog = false;
  expandedRows: { [key: string]: boolean } = {};

  constructor(
    private route: ActivatedRoute,
    private roomService: RoomService,
  ) {
  }

  ngOnInit() {
    this.roomId = this.route.snapshot.paramMap.get('id')!;
    if (this.roomId) {
      this.fetchRoomDetails(this.roomId);
    }
  }

  private fetchRoomDetails(roomId: string) {
    this.roomService.getRoomById(roomId).subscribe({
      next: (data) => {
        console.log('Room details: ', data);
        this.room = data;
        console.log('Room Threat: ', this.room.thresholds);
      },
      error: (error) => {
        console.error('Failed to load room details:', error);
      },
    });
  }

  onRowToggle(threshold: Threshold): void {
    console.log('Row toggled:', threshold);
    if (this.expandedRows[threshold.id]) {
      delete this.expandedRows[threshold.id];
    } else {
      this.expandedRows = {[threshold.id]: true};
    }
    console.log('Expanded rows:', this.expandedRows);
  }

  editThresholdDialog(threshold: Threshold) {
    console.log('Edit threshold dialog:', threshold);

  }

  onCellEditInit(threshold: Threshold) {
    console.log('Cell edit init:', threshold);
  }

  onCellEditSave(threshold: Threshold) {
    this.roomService.updateThreshold(threshold).subscribe({
      next: (data) => {
        console.log('Threshold updated: ', data);
      },
      error: (error) => {
        console.error('Failed to update threshold:', error);
      },
    });
  }

  onCellEditCancel(threshold: Threshold, index: number) {
    console.log('Cell edit cancel:', threshold, index);
  }
}
