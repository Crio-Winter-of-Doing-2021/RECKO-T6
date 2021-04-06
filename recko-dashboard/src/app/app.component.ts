import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from './services/recko-auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title: string = "recko-dashboard";

  constructor(private authService: ReckoAuthService, private router: Router) { }

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated;
  }

  isAdminAuthenticated(): boolean {
    return this.authService.isAdminAuthenticated;
  }

  isModeratorAuthenticated(): boolean {
    return this.authService.isModAuthenticated;
  }

  get authenticatedUser(): string {
    return this.authService.authenticatedUsername;
  }

  operatorSignOut() {
    this.authService.logout();
    this.router.navigate(["home"]);
  }
}
