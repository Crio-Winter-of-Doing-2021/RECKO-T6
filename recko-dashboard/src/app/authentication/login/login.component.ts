import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from '../../services/recko-auth.service';

import { IReckoOperator } from '../../models/recko-operator.model';
import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  operatorType: "admin" | "moderator" = "admin";
  operator: IReckoOperator = {
    username: null,
    password: null
  };

  isLoading: boolean = false;

  constructor(private authService: ReckoAuthService, private router: Router) { }

  ngOnInit(): void {
  }

  loginAdmin() {
    this.authService.loginAdmin(this.operator).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.setLoginTokens(true);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    });
  }

  loginModerator() {
    this.authService.loginModerator(this.operator).subscribe((response: IResponse) => {
      this.isLoading = false;
      window.alert(response.message);
      this.setLoginTokens(false);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    });
  }

  setLoginTokens(isAdmin: boolean = true) {
    this.authService.setTokens(this.operator, isAdmin);
    this.router.navigate(["consumer-list"]);
  }

  loginOperator() {
    this.isLoading = true;
    (this.operatorType === "admin") ? this.loginAdmin() : this.loginModerator();
  }

  cancelLogin() {
    this.router.navigate(["home"]);
  }

}
