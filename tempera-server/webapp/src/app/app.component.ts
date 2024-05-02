import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { StorageService } from './_services/storage.service';
import { AuthService } from './_services/auth.service';
import { NgIf } from '@angular/common';
import { PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  private roles: string[] = [];
  public isLoggedIn = false;
  public showAdminBoard = false;
  public showModeratorBoard = false;
  public username?: string;
  title: any = 'webapp';
  showAdminMenu = false;
  showModeratorMenu = false;

  toggleAdminMenu() {
    this.showAdminMenu = !this.showAdminMenu;
  }

  toggleModeratorMenu() {
    this.showModeratorMenu = !this.showModeratorMenu;
  }

  constructor(private storageService: StorageService, private authService: AuthService, private primengConfig: PrimeNGConfig) {
  }

  ngOnInit(): void {
    this.primengConfig.ripple = true;

    this.isLoggedIn = this.storageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.storageService.getUser();
      this.roles = user?.roles ?? [];

      this.showAdminBoard = this.roles.includes('ADMIN');
      this.showModeratorBoard = this.roles.includes('MANAGER');

      this.username = user?.username;
    }
  }

  public logout(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.storageService.clean();

        window.location.reload();
      },
      error: err => {
        console.log(err);
      },
    });
  }
}
