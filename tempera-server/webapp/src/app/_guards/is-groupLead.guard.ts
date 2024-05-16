import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../_services/storage.service';

export const isGroupLeadGuard: CanActivateFn = () => {
  const router = inject(Router);
  const storageService = inject(StorageService);

  if (storageService.getUser()?.roles.includes('GROUP_LEAD')) {
    return true;
  }

  router.navigate(['/']);

  return false;
};
