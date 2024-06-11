import {Component, OnInit} from '@angular/core';
import {ThresholdTipUpdateDto} from "../models/threshold.model";
import {RoomService} from "../_services/room.service";
import {ThresholdTip} from "../../api";
import {ToastModule} from "primeng/toast";
import {MessageService} from "primeng/api";
import {DialogModule} from "primeng/dialog";
import {TabViewModule} from "primeng/tabview";
import {NgForOf, NgIf} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextareaModule} from "primeng/inputtextarea";

@Component({
  selector: 'app-tips',
  standalone: true,
  imports: [
    ToastModule,
    DialogModule,
    TabViewModule,
    NgForOf,
    TableModule,
    ButtonModule,
    NgIf,
    FormsModule,
    InputTextareaModule,
    ReactiveFormsModule
  ],
  templateUrl: './tips.component.html',
  styleUrl: './tips.component.css'
})
export class TipsComponent implements OnInit{

  tips: ThresholdTip[] = [];
  selectedTip: ThresholdTip | undefined;
  tempTip!: ThresholdTip[];
  humTip!: ThresholdTip[];
  irrTip!: ThresholdTip[];
  nmTip!: ThresholdTip[];
  type: string[] = ['LOWER BOUND', 'UPPER BOUND'];
  viewEditDialog: boolean = false;
  constructor(private roomService: RoomService, private messageService: MessageService) {
  }
  ngOnInit() {
    this.roomService.getAllThresholdTips().subscribe(tips => {
      this.tips = tips;
      this.tempTip = this.tips.filter(tip => tip.id === -1 || tip.id === -2);
      this.humTip = this.tips.filter(tip => tip.id === -10 || tip.id === -11);
      this.irrTip = this.tips.filter(tip => tip.id === -20 || tip.id === -21);
      this.nmTip = this.tips.filter(tip => tip.id === -30);
      console.log(this.tempTip);
    });
  }

  showEditThresholdDialog(selectedTip: ThresholdTip) {
    this.selectedTip = selectedTip;
    this.viewEditDialog = true;
  }

  updateThresholdTip() {
    if (this.selectedTip !== undefined) {
      let tipUpdateDto: ThresholdTipUpdateDto = {
        id: this.selectedTip.id!,
        tip: this.selectedTip.tip!,
      };
      this.roomService.updateThresholdTip(tipUpdateDto).subscribe(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Threshold tip updated successfully'
        });
        this.selectedTip = undefined;
        this.viewEditDialog = false;
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Error', detail: 'Failed to update threshold tip'});
      });
    }
  }
}
