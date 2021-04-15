import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoAuthService } from '../../services/recko-auth.service';

import { IReckoOperator } from '../../models/recko-operator.model';
import { IResponse } from '../../models/response.model';
import { StorageKey } from '../../models/storage-key.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  operatorType: "admin" | "moderator" = "admin";
  operator: IReckoOperator = {
    username: null,
    password: null,
    company: {
      id: null
    }
  };

  isLoading: boolean = false;

  constructor(private authService: ReckoAuthService, private router: Router) {
    this.operator.company.id = localStorage.getItem(StorageKey.company);
  }

  ngOnInit(): void {
  }

  loginAdmin() {
    this.authService.loginAdmin(this.operator).subscribe((response: IReckoOperator) => {
      this.isLoading = false;
      window.alert("Admin Logged In Successfully");
      this.setLoginTokens(response, true);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    });
  }

  loginModerator() {
    this.authService.loginModerator(this.operator).subscribe((response: IReckoOperator) => {
      this.isLoading = false;
      window.alert("Moderator Logged In Successfully");
      this.setLoginTokens(response, false);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    });
  }

  setLoginTokens(response: IReckoOperator, isAdmin: boolean) {
    this.authService.setTokens(response, isAdmin);
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
