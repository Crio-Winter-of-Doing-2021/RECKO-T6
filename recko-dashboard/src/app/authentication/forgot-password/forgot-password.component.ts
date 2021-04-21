import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from '../../services/recko-auth.service';
import { CompanyService } from 'src/app/services/company.service';

import { ICompanyHandler } from '../../models/company-handler.model';
import { IResponse } from 'src/app/models/response.model';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  roles: string[] = [];
  selectedRole: string = null;

  readonly handler: ICompanyHandler = {
    username: null,
    email: null
  };

  isLoading: boolean = false;

  constructor(private router: Router, private authService: ReckoAuthService, private companyService: CompanyService) { }

  ngOnInit(): void {
    this.fetchCompanyHandlerRoles();
  }

  private fetchCompanyHandlerRoles() {
    this.isLoading = true;

    this.companyService.fetchCompanyHandlerRoles().subscribe((roles: string[]) => {
      this.isLoading = false;
      this.roles = roles;
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    });
  }

  private adminForgotPassword() {
    this.isLoading = true;

    this.authService.forgotPasswordAdmin(this.handler).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.router.navigate(["login"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    })
  }

  private moderatorForgotPassword() {
    this.isLoading = true;

    this.authService.forgotPasswordModerator(this.handler).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.router.navigate(["login"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    })
  }

  handlerForgotPassword() {
    if (this.selectedRole !== null) {
      this.selectedRole = this.selectedRole.trim().toLowerCase();
      this.selectedRole === "admin" ? this.adminForgotPassword() : this.moderatorForgotPassword();
    }
  }

  cancelForgotPassword() {
    this.router.navigate(["home"]);
  }

}
