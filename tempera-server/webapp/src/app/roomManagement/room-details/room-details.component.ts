import {Component, OnInit} from '@angular/core';
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
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {AccessPoint} from "../../models/accessPoint.model";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import _default from "chart.js/dist/plugins/plugin.tooltip";
import numbers = _default.defaults.animations.numbers;

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
    InputTextModule,
    ToastModule,
    ReactiveFormsModule
  ],
  templateUrl: './room-details.component.html',
  styleUrl: './room-details.component.css'
})
/**
 * @class RoomDetailsComponent
 * This component is for managing thresholds and displaying room details.
 */
export class RoomDetailsComponent implements OnInit{
  private roomId!: string;
  room: Room | undefined;
  accessPoint: AccessPoint | undefined;
  displayEditThresholdDialog = false;
  selectedThreshold: Threshold | undefined;
  filteredThresholds: Threshold[] = [];
  thresholdForm: FormGroup;

  constructor( private route: ActivatedRoute,
               private roomService: RoomService,
               private messageService: MessageService,
               fb: FormBuilder) {
    this.thresholdForm = fb.group({
      value: ['', Validators.required],
      reason: ['', Validators.minLength(6)],
    });
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
        this.room = data;
        this.filteredThresholds = this.room.thresholds;
        this.fetchAccesspoint();
      },
      error: (error) => {
        console.error('Failed to load room details:', error);
      },
    });
  }
  private fetchAccesspoint() {
    if (this.room) {
      this.roomService.getAccessPoint(this.room.id).subscribe({
        next: (data) => {
          this.accessPoint = data;
        },
        error: (error) => {
          console.error('Failed to load access point:', error);
        },
      });
    }
  }
  /**
   * This method is called when a threshold is edited.
   * It sets the selected threshold and displays the edit threshold dialog.
   * @param threshold
   */
  editThreshold(threshold: Threshold) {
    this.thresholdForm.patchValue({
      value: threshold.value});
    this.selectedThreshold = threshold;
    this.displayEditThresholdDialog = true;
  }
  /**
   * This method is called when the edit threshold dialog is closed.
   * It resets the selected threshold and reason.
   */
  onSubmit() {
      if (this.thresholdForm.valid && this.selectedThreshold) {
        this.selectedThreshold.value = this.thresholdForm.value.value;
        const dto: ThresholdUpdateDto = {
          threshold: this.selectedThreshold,
          reason: this.thresholdForm.value.reason,
        };
        this.roomService.updateThreshold(dto).subscribe({
          next: (data) => {
            console.log('Threshold updated: ', data);
            this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Threshold updated successfully' });
            this.selectedThreshold = undefined;
            this.thresholdForm.reset();
            this.displayEditThresholdDialog = false;
          },
          error: (error) => {
            console.error('Failed to update threshold:', error);
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to update threshold' });
          },
        });
      }
  }
  closeEditThresholdDialog() {
    this.selectedThreshold = undefined;
    this.thresholdForm.reset();
    this.displayEditThresholdDialog = false;
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
