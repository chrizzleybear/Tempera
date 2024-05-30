import { Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, tap } from 'rxjs';
import { WarningControllerService, WarningDto } from '../../api';

@Injectable({
  providedIn: 'root',
})
export class WarningStoreService {
  private warnings$ = new BehaviorSubject<WarningDto[]>([]);


  constructor(private warningControllerService: WarningControllerService) {
    // initial fetch
    this.fetchWarnings();
  }

  getWarnings() {
    // todo: remove logging
    return this.warnings$.pipe(distinctUntilChanged());
  }

  removeWarning(id: string) {
    // remove directly from the store and then call the api to increase responsiveness
    this.warnings$.next([...this.warnings$.value.filter(w => w.id !== id)]);
    this.warningControllerService.deleteWarning(id).subscribe({
      error: err => {
        // todo: handle /display error
        console.log(err);
      },
    });


  }

  refreshWarnings() {
    this.fetchWarnings();
  }

  private fetchWarnings() {
    this.warningControllerService.getWarnings().subscribe({
      next: res => {
        this.warnings$.next(res);
      },
      error: err => {
        console.log(err);
      },
    });
  }

}
