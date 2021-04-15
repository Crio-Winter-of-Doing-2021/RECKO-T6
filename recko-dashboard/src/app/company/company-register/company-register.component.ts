import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CompanyService } from '../../services/company.service';

import { ICompanyRegister } from '../../models/company-register.model';
import { ICompany } from '../../models/company.model';
import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-company-register',
  templateUrl: './company-register.component.html',
  styleUrls: ['./company-register.component.css']
})
export class CompanyRegisterComponent implements OnInit {

  readonly company: ICompanyRegister = {
    id: null,
    name: null,
    password: null
  }

  isLoading: boolean = false;

  constructor(private companyService: CompanyService, private router: Router) { }

  ngOnInit(): void {
  }

  registerCompany() {
    this.isLoading = true;

    this.companyService.registerCompany(this.company).subscribe((registeredCompany: ICompany) => {
      this.isLoading = false;

      if (registeredCompany.id === this.company.id) {
        window.alert("Company Registered Successfully");
        this.router.navigate(["home"]);
      } else {
        window.alert("Company Couldn't Be Registered");
      }
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message);
    })
  }

  cancelRegisterCompany() {
    this.router.navigate(["home"]);
  }

}
