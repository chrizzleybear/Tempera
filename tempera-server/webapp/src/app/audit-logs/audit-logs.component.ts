import { Component, OnInit } from '@angular/core';
import { AuditLogControllerService, AuditLogDto } from '../../api';
import { TableModule } from 'primeng/table';
import { DatePipe, NgIf } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MultiSelectModule } from 'primeng/multiselect';
import { AuditLog } from '../../api/model/auditLog';
import ActionTypeEnum = AuditLog.ActionTypeEnum;
import AffectedTypeEnum = AuditLog.AffectedTypeEnum;
import { CalendarModule } from 'primeng/calendar';
import { FormsModule } from '@angular/forms';
import { FilterService } from 'primeng/api';

interface InternalAuditLogDto extends AuditLogDto {
  time: Date;
}

@Component({
  selector: 'app-audit-logs',
  standalone: true,
  imports: [
    TableModule,
    NgIf,
    ProgressSpinnerModule,
    MultiSelectModule,
    DatePipe,
    CalendarModule,
    FormsModule,
  ],
  templateUrl: './audit-logs.component.html',
  styleUrl: './audit-logs.component.css',
})
export class AuditLogsComponent implements OnInit {
  public auditLogs?: InternalAuditLogDto[];
  public actionTypeOptions: ActionTypeEnum[] = Object.values(ActionTypeEnum);
  public affectedTypeOptions: AffectedTypeEnum[] = Object.values(AffectedTypeEnum);
  rangeDates: any;

  constructor(private auditLogControllerService: AuditLogControllerService, private filterService: FilterService) {
  }

  ngOnInit(): void {

    /*
    Custom filter for date range
     */
    this.filterService.register('range-filter', (value: any, filter: any): boolean => {
      if (filter === undefined || filter === null || filter[1] === undefined || filter[1] === null) {
        return true;
      }

      if (value === undefined || value === null) {
        return false;
      }

      return (this.rangeDates[0] <= value && this.rangeDates[1] >= value);

    });

    this.auditLogControllerService.getAllAuditLogs().subscribe({
        next: auditLogs => {
          this.auditLogs = auditLogs.map(a => ({ ...a, time: new Date(a.timeStamp) }));
        },
        error: error => {
          console.error('There was an error!', error);
        },
      },
    );
  }

}
