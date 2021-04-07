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
  accountHolderFilter: string = null;
  creationDateFilter: string = null;

  loadedContent: number = 2;

  private holderAscending = false;
  private typeAscending = false;
  private amountAscending = false;
  private dateAscending = false;

  readonly itemsPerPage = 15;
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
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
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
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
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
      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
      this.router.navigate(["**"]);
    })
  }

  deleteConsumer(consumer: IReckoConsumer) {
    const index = this.consumers.indexOf(consumer);
    if (index !== -1) {
      this.consumers.splice(index, 1);
    }
  }

  applyFilters() {
    if (this.accountHolderFilter !== null && this.accountHolderFilter.length !== 0) {
      this.consumers = this.consumers.filter((consumer: IReckoConsumer) => {
        return consumer.name.toLowerCase().indexOf(this.accountHolderFilter.toLowerCase()) > -1;
      });
    }

    if (this.creationDateFilter !== null) {
      this.consumers = this.consumers.filter((consumer: IReckoConsumer) => {
        return consumer.date.toString() === this.creationDateFilter;
      });
    }
  }

  resetFilters() {
    if (this.selectedPartner === null) this.fetchAllConsumers();
    else this.filterPartnerConsumers();
  }

  disableFilters(): boolean {
    return (this.accountHolderFilter === null || this.accountHolderFilter.length === 0)
      && (this.creationDateFilter === null || this.creationDateFilter.length === 0);
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

  private compareConsumerDate(a: string, b: string): number {
    if (a !== b) {
      const aElements: number[] = a.split("-").map(d => parseInt(d));
      const bElements: number[] = b.split("-").map(d => parseInt(d));

      if (aElements[0] !== bElements[0]) return aElements[0] - bElements[0];
      else {
        if (aElements[1] !== bElements[1]) return aElements[1] - bElements[1];
        else {
          if (aElements[2] !== bElements[2]) return aElements[2] - bElements[2];
          else return 0;
        }
      }
    }

    return 0;
  }

  changeDateOrder() {
    this.dateAscending = !this.dateAscending;
    this.consumers.sort((a, b) => {
      return (this.dateAscending) ? this.compareConsumerDate(a.date.toString(), b.date.toString())
        : this.compareConsumerDate(b.date.toString(), a.date.toString());
    })
  }
}