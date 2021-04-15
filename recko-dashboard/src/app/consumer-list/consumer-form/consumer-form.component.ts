import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoConsumerService } from '../../services/recko-consumer.service';
import { ReckoPartnerService } from '../../services/recko-partner.service';

import { IReckoConsumer } from '../../models/recko-consumer.model';
import { IReckoPartner } from '../../models/recko-partner.model';
import { IResponse } from '../../models/response.model';
import { StorageKey } from '../../models/storage-key.model';

@Component({
  selector: 'app-consumer-form',
  templateUrl: './consumer-form.component.html',
  styleUrls: ['./consumer-form.component.css']
})
export class ConsumerFormComponent implements OnInit {

  consumer: IReckoConsumer = {
    id: null,
    name: null,
    amount: null,
    date: null,
    type: null,
    credential: {
      email: null,
      password: null,
      company: {
        id: null
      },
      partner: {
        name: null,
        description: null
      }
    }
  };

  isLoading: boolean = false;

  partners: IReckoPartner[] = [];
  accountTypes: string[] = [];

  selectedPartner: string = null;
  selectedAccountType: string = null;

  constructor(private router: Router, private consumerService: ReckoConsumerService, private partnerService: ReckoPartnerService) {
    const queryParams = this.router.getCurrentNavigation().extras.queryParams;
    if (queryParams) {
      const consumer = <IReckoConsumer>queryParams.consumer;
      this.consumer = consumer;
      this.selectedPartner = consumer.credential.partner.name;
      this.selectedAccountType = consumer.type;
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

  submitConsumer() {
    this.consumer.credential.partner.name = this.selectedPartner;
    this.consumer.type = this.selectedAccountType;
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