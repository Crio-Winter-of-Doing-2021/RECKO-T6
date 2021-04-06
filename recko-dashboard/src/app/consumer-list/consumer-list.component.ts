import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoConsumerService } from '../services/recko-consumer.service';
import { ReckoPartnerService } from '../services/recko-partner.service';

import { IReckoConsumer } from '../models/recko-consumer.model';
import { IReckoPartner } from '../models/recko-partner.model';
import { IResponse } from '../models/response.model';

@Component({
  selector: 'app-consumer-list',
  templateUrl: './consumer-list.component.html',
  styleUrls: ['./consumer-list.component.css']
})
export class ConsumerListComponent implements OnInit {

  consumers: IReckoConsumer[] = [];
  partners: IReckoPartner[] = [];

  selectedPartner: string = null;

  loadedContent: number = 2;

  private holderAscending = false;
  private typeAscending = false;
  private amountAscending = false;
  private providerAscending = false;

  readonly itemsPerPage = 20;
  currentPage = 1;
  readonly maxPaginationSize = 100;

  constructor(private consumerService: ReckoConsumerService,
    private partnerService: ReckoPartnerService,
    private router: Router) {
  }

  ngOnInit() {
    this.fetchAllConsumers();
    this.fetchAllPartners();
  }

  fetchAllConsumers() {
    this.loadedContent--;

    this.consumerService.getConsumers().subscribe(consumers => {
      this.consumers = consumers;
      this.loadedContent++;
    }, (error: IResponse) => {
      this.loadedContent++;
      window.alert(error.message);
      this.router.navigate(["**"]);
    })
  }

  fetchAllPartners() {
    this.loadedContent--;

    this.partnerService.getPartners().subscribe((partners: IReckoPartner[]) => {
      this.partners = partners;
      this.loadedContent++;
    }, (error: IResponse) => {
      this.loadedContent++;
      window.alert(error.message);
      this.router.navigate(["**"]);
    })
  }

  filterPartnerConsumers() {
    this.loadedContent--;

    this.consumerService.getPartnerConsumers(this.selectedPartner).subscribe(consumers => {
      this.consumers = consumers;
      this.loadedContent++;
    }, (error: IResponse) => {
      this.loadedContent++;
      window.alert(error.message);
      this.router.navigate(["**"]);
    })
  }

  deleteConsumer(consumer: IReckoConsumer) {
    const index = this.consumers.indexOf(consumer);
    if (index !== -1) {
      this.consumers.splice(index, 1);
    }
  }

  changeHolderOrder() {
    this.holderAscending = !this.holderAscending;
    this.consumers.sort((a, b) => {
      return (this.holderAscending)
        ? a.name.toLowerCase().localeCompare(b.name.toLowerCase())
        : b.name.toLowerCase().localeCompare(a.name.toLowerCase());
    });
  }

  changeTypeOrder() {
    this.typeAscending = !this.typeAscending;
    this.consumers.sort((a, b) => {
      return (this.typeAscending)
        ? (a.type.toLowerCase().localeCompare(b.type.toLowerCase()))
        : (b.type.toLowerCase()).localeCompare(a.type.toLowerCase());
    });
  }

  changeAmountOrder() {
    this.amountAscending = !this.amountAscending;
    this.consumers.sort((a, b) => {
      return (this.amountAscending) ? a.amount - b.amount : b.amount - a.amount;
    });
  }

  changeProviderOrder() {
    this.providerAscending = !this.providerAscending;
    this.consumers.sort((a, b) => {
      return (this.providerAscending)
        ? a.credential.partner.name.toLowerCase().localeCompare(b.credential.partner.name.toLowerCase())
        : b.credential.partner.name.toLowerCase().localeCompare(a.credential.partner.name.toLowerCase());
    })
  }

}
