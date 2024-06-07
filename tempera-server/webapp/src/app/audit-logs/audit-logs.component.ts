import { Component, OnInit } from '@angular/core';
import { AuditLogControllerService, AuditLogDto } from '../../api';
import { TableModule } from 'primeng/table';
import { DatePipe, NgIf } from '@angular/common';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { MultiSelectModule } from 'primeng/multiselect';
import { AuditLog } from '../../api/model/auditLog';
import ActionTypeEnum = AuditLog.ActionTypeEnum;
import AffectedTypeEnum = AuditLog.AffectedTypeEnum;

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
  ],
  templateUrl: './audit-logs.component.html',
  styleUrl: './audit-logs.component.css',
})
export class AuditLogsComponent implements OnInit {
  public auditLogs?: InternalAuditLogDto[];
  public actionTypeOptions: ActionTypeEnum[] = Object.values(ActionTypeEnum);
  public affectedTypeOptions: AffectedTypeEnum[] = Object.values(AffectedTypeEnum);

  constructor(private auditLogControllerService: AuditLogControllerService) {
  }

  ngOnInit(): void {
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
