import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CompanyService } from '../../services/company.service';

import { IReckoOperatorRegister } from '../../models/operator-register.model';
import { IReckoOperator } from '../../models/recko-operator.model';
import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-company-handler-register',
  templateUrl: './company-handler-register.component.html',
  styleUrls: ['./company-handler-register.component.css']
})
export class CompanyHandlerRegisterComponent implements OnInit {

  readonly handler: IReckoOperatorRegister = {
    companyId: null,
    companyPassword: null,
    handlerName: null,
    handlerPassword: null
  };

  roles: string[] = [];
  selectedRole: string = null;

  isLoading: boolean = false;

  constructor(private router: Router, private companyService: CompanyService) { }

  ngOnInit(): void {
    this.fetchCompanyHandlerRoles();
  }

  fetchCompanyHandlerRoles() {
    this.companyService.fetchCompanyHandlerRoles().subscribe((roles: string[]) => {
      this.roles = roles;
    }, (error: IResponse) => {
      window.alert(error.message);
      this.router.navigate(["home"]);
    })
  }

  registerCompanyAdmin() {
    this.isLoading = true;

    this.companyService.registerAdmin(this.handler).subscribe((operator: IReckoOperator) => {
      this.isLoading = false;

      if (operator.username === this.handler.handlerName && operator.company.id === this.handler.companyId) {
        window.alert("Admin Registered Successfully");
      } else {
        window.alert("Admin Couldn't Be Registered");
      }

      this.router.navigate(["home"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    })
  }

  registerCompanyModerator() {
    this.isLoading = true;

    this.companyService.registerModerator(this.handler).subscribe((operator: IReckoOperator) => {
      this.isLoading = false;
      if (operator.username === this.handler.handlerName && operator.company.id === this.handler.companyId) {
        window.alert("Moderator Registered Successfully");
      } else {
        window.alert("Moderator Couldn't Be Registered");
      }

      this.router.navigate(["home"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    })
  }

  registerCompanyHandler() {
    (this.selectedRole.toLowerCase() === "admin")
      ? this.registerCompanyAdmin()
      : this.registerCompanyModerator();
  }

  cancelRegisterCompnayHandler() {
    this.router.navigate(["home"]);
  }
}
