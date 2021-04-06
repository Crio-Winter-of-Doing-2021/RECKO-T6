import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from '../../services/recko-auth.service';

import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  operatorName: string;
  operatorType: "admin" | "moderator" = "admin";

  constructor(private authService: ReckoAuthService, private router: Router) { }

  ngOnInit(): void {
  }

  isLoading: boolean = false;

  adminForgotPassword() {
    this.authService.forgotPasswordAdmin(this.operatorName).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(`Your Password is ${response.message}`);
      this.router.navigate(["login"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    });
  }

  moderatorForgotPassword() {
    this.authService.forgotPasswordModerator(this.operatorName).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(`Your Password is ${response.message}`);
      this.router.navigate(["login"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    });
  }

  forgotPassword() {
    this.isLoading = true;
    if (this.operatorType === "admin") this.adminForgotPassword();
    else if (this.operatorType === "moderator") this.moderatorForgotPassword();
  }

  cancelForgotPassword() {
    this.router.navigate(["login"]);
  }

}
