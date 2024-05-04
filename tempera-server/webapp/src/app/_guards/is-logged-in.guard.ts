import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../_services/storage.service';

export const isLoggedInGuard: CanActivateFn = () => {
  const router = inject(Router);
  const storageService = inject(StorageService);

  if (storageService.isLoggedIn()) {
    return true;
  }

  router.navigate(['/login']);

  return false;
};
