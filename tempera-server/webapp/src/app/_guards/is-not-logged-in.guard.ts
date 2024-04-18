import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../_services/storage.service';

export const isNotLoggedInGuard: CanActivateFn = () => {
  const storageService = inject(StorageService);
  const routerService = inject(Router);

  if (!storageService.isLoggedIn()) {
    return true;
  }

  routerService.navigate(['/']);
  return false;
};
