import { Injectable } from '@angular/core';
import { BehaviorSubject, distinctUntilChanged, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WarningStoreService {
  private warnings$ = new BehaviorSubject<string[]>([]);

  constructor() {
    // todo: call the api here to get initial warnings
    this.warnings$.next(['oh helloooooooooooooooooooooooooooooooooooooooooooooooooooo', 'world']);
  }

  getWarnings() {
    // todo: remove logging
    return this.warnings$.pipe(distinctUntilChanged(), tap(() => console.log('warnings updated')));
  }

  removeWarning(warning: string) {
    this.warnings$.next([...this.warnings$.value.filter(w => w !== warning)]);
  }

  refreshWarnings() {
    this.warnings$.next([...this.warnings$.value, 'new warning']);
  }

}
