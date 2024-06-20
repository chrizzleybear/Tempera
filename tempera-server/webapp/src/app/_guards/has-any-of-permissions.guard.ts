import { Router } from '@angular/router';
import { inject } from '@angular/core';
import { StorageService } from '../_services/storage.service';
import { UserxDto } from '../../api';
import RolesEnum = UserxDto.RolesEnum;

export function hasAnyOfPermissionsGuard(roles: RolesEnum[]) {
  return () => {
    const router = inject(Router);
    const storageService = inject(StorageService);

    if (roles.some(x => storageService.getUser()?.roles.includes(x as RolesEnum))) {
      return true;
    }

    router.navigate(['/']);

    return false;
  }
}
