import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  clean(): void {
    window.sessionStorage.clear();
  }

  public saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): User | undefined {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }
    console.error('Active user could not be found');
    return undefined;
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    return !!user;
  }

}
