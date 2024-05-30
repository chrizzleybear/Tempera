import { Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged } from 'rxjs';
import { AlertControllerService, AlertDto } from '../../api';

@Injectable({
  providedIn: 'root',
})
export class AlertStoreService {
  private alerts$ = new BehaviorSubject<AlertDto[]>([]);


  constructor(private alertControllerService: AlertControllerService) {
  }

  getAlerts() {
    return this.alerts$.pipe(distinctUntilChanged());
  }

  removeAlert(id: string) {
    // remove directly from the store and then call the api to increase responsiveness
    this.alerts$.next([...this.alerts$.value.filter(w => w.id !== id)]);
    this.alertControllerService.deleteAlert(id).subscribe({
      error: err => {
        // todo: handle /display error
        console.log(err);
      },
    });


  }

  refreshAlerts() {
    this.fetchAlerts();
  }

  private fetchAlerts() {
    this.alertControllerService.getAlerts().subscribe({
      next: res => {
        this.alerts$.next(res);
      },
      error: err => {
        console.log(err);
      },
    });
  }

}
