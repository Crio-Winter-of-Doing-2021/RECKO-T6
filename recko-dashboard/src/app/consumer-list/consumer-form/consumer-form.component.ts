import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoConsumerService } from '../../services/recko-consumer.service';
import { ReckoPartnerService } from '../../services/recko-partner.service';
import { CompanyService } from '../../services/company.service';

import { IReckoConsumer } from '../../models/recko-consumer.model';
import { IReckoPartner } from '../../models/recko-partner.model';
import { IResponse } from '../../models/response.model';
import { StorageKey } from '../../models/storage-key.model';
import { ICompanyCredential } from '../../models/company-credential.model';

@Component({
  selector: 'app-consumer-form',
  templateUrl: './consumer-form.component.html',
  styleUrls: ['./consumer-form.component.css']
})
export class ConsumerFormComponent implements OnInit {

  consumer: IReckoConsumer = {
    id: null, name: null, amount: null, date: null, type: null,
    credential: {
      id: null, name: null, email: null, password: null,
      company: {
        id: null
      },
      partner: {
        name: null, description: null
      }
    }
  };

  isLoading: boolean = false;

  partners: IReckoPartner[] = [];
  accountTypes: string[] = [];
  credentials: ICompanyCredential[] = [];

  selectedPartner: string = null;
  selectedAccountType: string = null;
  selectedCredential: number = null;

  constructor(private router: Router,
    private consumerService: ReckoConsumerService,
    private partnerService: ReckoPartnerService,
    private companyService: CompanyService) {
    const queryParams = this.router.getCurrentNavigation().extras.queryParams;
    if (queryParams) {
      const consumer = <IReckoConsumer>queryParams.consumer;
      this.consumer = consumer;

      this.selectedPartner = consumer.credential.partner.name;
      this.selectedAccountType = consumer.type;
      this.selectedCredential = consumer.credential.id;
    }

    this.consumer.credential.company.id = localStorage.getItem(StorageKey.company);
  }

  ngOnInit(): void {
    this.fetchPartners();
  }

  fetchPartners() {
    this.partnerService.getPartners().subscribe((partners: IReckoPartner[]) => {
      this.partners = partners;
    }, (error: IResponse) => {
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
      this.router.navigate(["**"]);
    })
  }

  onPartnerChanged() {
    this.fetchAccountTypes();
    this.fetchCompanyCredentials();
  }

  fetchAccountTypes() {
    if (this.selectedPartner !== null) {
      this.consumerService.getConsumerTypes(this.selectedPartner).subscribe((types: string[]) => {
        this.accountTypes = types;
      }, (error: IResponse) => {
        window.alert(error.message || "Unable to connect to third party services, please refresh the page");
        this.router.navigate(["consumer-list"]);
      });
    }
  }

  fetchCompanyCredentials() {
    if (this.selectedPartner !== null) {
      this.companyService.fetchCompanyCredentials(this.selectedPartner).subscribe((data: ICompanyCredential[]) => {
        this.credentials = data;
      }, (error: IResponse) => {
        window.alert(error.message);
        this.router.navigate(["consumer_list"]);
      })
    }
  }

  submitConsumer() {
    this.consumer.credential.partner.name = this.selectedPartner;
    this.consumer.type = this.selectedAccountType;
    this.consumer.credential.id = this.selectedCredential;
    this.isLoading = true;
    (this.consumer.id === null) ? this.addConsumer() : this.updateConsumer();
  }

  updateConsumer() {
    this.consumerService.updateConsumer(this.consumer).subscribe((response: IReckoConsumer) => {
      this.isLoading = false;
      window.alert("Consumer Updated Successfully");
      this.router.navigate(["consumer-list"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    })
  }

  addConsumer() {
    this.consumerService.addConsumer(this.consumer).subscribe((response: IReckoConsumer) => {
      this.isLoading = false;
      window.alert("Consumer Added Successfully");
      this.router.navigate(["consumer-list"]);
    }, (error: IResponse) => {
      this.isLoading = false;
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
    });
  }

  cancelAddConsumer() {
    this.router.navigate(["consumer-list"]);
  }

  submitButtonValue(): string {
    return this.consumer.id !== null ? "Update Consumer" : "Add Consumer";
  }

}