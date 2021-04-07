import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from '../../services/recko-auth.service';

import { IChangePassword } from '../../models/change-password.model';
import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  operatorPass: IChangePassword = {
    username: null,
    oldPassword: null,
    newPassword: null
  };

  isLoading: boolean = false;

  constructor(private authService: ReckoAuthService, private router: Router) {
    this.operatorPass.username = this.authService.authenticatedUsername;
  }

  ngOnInit(): void { }

  changePasswordAdmin() {
    this.authService.changePasswordAdmin(this.operatorPass).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.logout();
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    });
  }

  changePasswordModerator() {
    this.authService.changePasswordModerator(this.operatorPass).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.logout();
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(["login"]);
  }

  changePassword() {
    this.isLoading = true;
    if (this.authService.isAdminAuthenticated) this.changePasswordAdmin();
    else if (this.authService.isModAuthenticated) this.changePasswordModerator();
  }

  cancelChangePassword() {
    this.router.navigate(["consumer-list"]);
  }

}
