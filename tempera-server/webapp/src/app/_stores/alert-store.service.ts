import { DestroyRef, Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, exhaustMap, timer } from 'rxjs';
import { AlertControllerService, AlertDto } from '../../api';
import { MessageService } from 'primeng/api';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Injectable({
  providedIn: 'root',
})
export class AlertStoreService {
  private alerts$ = new BehaviorSubject<AlertDto[]>([]);


  constructor(private alertControllerService: AlertControllerService, private messageService: MessageService, private destroyRef: DestroyRef) {
  }

  getAlerts() {
    return this.alerts$.pipe(distinctUntilChanged());
  }

  removeAlert(id: string) {
    // get the alert to remove (in case deletion fails)
    const alert = this.alerts$.value.find(w => w.id === id);

    // remove directly from the store and then call the api to increase responsiveness
    this.alerts$.next([...this.alerts$.value.filter(w => w.id !== id)]);
    this.alertControllerService.deleteAlert(id).subscribe({
      error: err => {
        // add back to store
        this.alerts$.next([...this.alerts$.value, alert!]);
        this.messageService.add({
          key: 'topbar-toast',
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to delete alert',
        });
        console.log(err);
      },
    });
  }

  /*
    * Start a timer that fetches alerts every 20 seconds
    * The usage of exhaustMap ensures that a new request is only made if the previous one has completed. Otherwise, the request is ignored.
   */
  startAlertTimer() {
    timer(0, 1000 * 20).pipe(
      exhaustMap(() => this.alertControllerService.getAlerts()),
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: res => {
          this.alerts$.next(res);
        },
        error: err => {
          console.log(err);
        },
      });
  }

  refreshAlerts() {
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
