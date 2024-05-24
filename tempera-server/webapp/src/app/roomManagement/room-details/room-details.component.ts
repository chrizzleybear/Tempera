import {Component} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RoomService} from "../../_services/room.service";
import {Room} from "../../models/room.model";
import {TableModule} from "primeng/table";
import {CardModule} from "primeng/card";
import {NgForOf, NgIf} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {Threshold, ThresholdTipUpdateDto, ThresholdUpdateDto} from "../../models/threshold.model";
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
  selectedThreshold: Threshold | undefined;
  reason: string = '';
  filteredThresholds: Threshold[] = [];
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
        this.filteredThresholds = this.room.thresholds;
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
  onCellEditSave(threshold: Threshold) {
    this.displayEditThresholdDialog = true;
    this.selectedThreshold = threshold;
  }

  editThreasholdSave() {
    if (this.selectedThreshold && this.reason !== '') {
      const dto : ThresholdUpdateDto = {
        threshold: this.selectedThreshold,
        reason: this.reason,
      };
      this.roomService.updateThreshold(dto).subscribe({
        next: (data) => {
          console.log('Threshold updated: ', data);
          this.selectedThreshold = undefined;
          this.reason = '';
          this.displayEditThresholdDialog = false;
        },
        error: (error) => {
          console.error('Failed to update threshold:', error);
        },
      });
    }
  }
  editThresholdTipSave() {
    if (this.selectedThreshold) {
    const dto : ThresholdTipUpdateDto = {
      id: this.selectedThreshold.id,
      tip: this.selectedThreshold.tip.tip,
    };
    this.roomService.updateThresholdTip(dto).subscribe({
      next: (data) => {
        console.log('Threshold updated: ', data);
      },
      error: (error) => {
        console.error('Failed to update threshold:', error);
      },
    });
      this.selectedThreshold = undefined;
      this.reason = '';
      this.displayEditThresholdDialog = false;
  }
  }
  onCellEditSaveTip(threshold: Threshold) {
    this.selectedThreshold = threshold;
    this.editThresholdTipSave();
  }
  globalFilter(event: any) {
    const filterValue = event.target.value.toLowerCase();
    this.filteredThresholds = this.room?.thresholds.filter(threshold =>
      threshold.id.toString().toLowerCase().includes(filterValue) ||
      threshold.sensorType.toLowerCase().includes(filterValue) ||
      threshold.thresholdType.toLowerCase().includes(filterValue) ||
      threshold.value.toString().toLowerCase().includes(filterValue) ||
      threshold.tip.tip.toLowerCase().includes(filterValue)
    )!;
  }
}
