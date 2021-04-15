import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { CompanyService } from '../../services/company.service';
import { ReckoPartnerService } from '../../services/recko-partner.service';

import { ICompanyCredentialRegister } from '../../models/company-credential-register.model';
import { ICompanyCredential } from '../../models/company-credential.model';
import { IResponse } from '../../models/response.model';
import { IReckoPartner } from '../..//models/recko-partner.model';

@Component({
  selector: 'app-company-credential-register',
  templateUrl: './company-credential-register.component.html',
  styleUrls: ['./company-credential-register.component.css']
})
export class CompanyCredentialRegisterComponent implements OnInit {

  readonly cred: ICompanyCredentialRegister = {
    applicationId: null, companyId: null, companyPassword: null, email: null, password: null,
    refreshToken: null, remoteAccountId: null, remoteAccountSecret: null, scope: null
  };

  partners: IReckoPartner[] = [];
  selectedPartner: string = null;

  isLoading: boolean = false;

  constructor(private router: Router,
    private companyService: CompanyService,
    private partnerService: ReckoPartnerService) { }

  ngOnInit(): void {
    this.fetchPartners();
  }

  fetchPartners() {
    this.partnerService.getPartners().subscribe((data: IReckoPartner[]) => {
      this.partners = data;
    }, (error: IResponse) => {
      window.alert(error.message);
      this.router.navigate(["home"]);
    })
  }

  registerCompanyCredential() {
    this.isLoading = true;

    this.companyService.registerCompanyCredential(this.cred, this.selectedPartner)
      .subscribe((response: ICompanyCredential) => {
        this.isLoading = false;
        if (response.company.id === this.cred.companyId) {
          window.alert("Credential Registered Successfully");
        } else {
          window.alert("Credential Couldn't be Registered");
        }

        this.router.navigate(["home"]);
      }, (error: IResponse) => {
        this.isLoading = false;
        window.alert(error.message);
      });
  }

  cancelRegisterCompanyCredential() {
    this.router.navigate(["home"]);
  }

}
