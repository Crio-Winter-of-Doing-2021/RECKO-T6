import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoTransactionService } from '../services/recko-transaction.service';
import { ReckoPartnerService } from '../services/recko-partner.service';

import { ITransaction } from '../models/transaction.model';
import { IResponse } from '../models/response.model';
import { IReckoPartner } from '../models/recko-partner.model';

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.css']
})
export class TransactionListComponent implements OnInit {

  transactions: ITransaction[] = [];
  partners: IReckoPartner[] = [];

  selectedPartner: string = null;

  contentLoaded: number = 2;

  holderAscending = null;
  receiverAscending = null;
  typeAscending = null;
  amountAscending = null;
  dateAscending = false;

  readonly itemsPerPage = 15;
  currentPage: number = 1;
  readonly maxPaginationSize = 100;

  constructor(private transactionService: ReckoTransactionService, private partnerService: ReckoPartnerService, private router: Router) { }

  ngOnInit(): void {
    this.fetchTransactions();
    this.fetchPartners();
  }

  private resetOrderFilters() {
    this.holderAscending = this.receiverAscending = this.amountAscending = this.typeAscending = null;
    this.dateAscending = false;
  }

  fetchTransactions() {
    this.contentLoaded--;
    this.resetOrderFilters();

    this.transactionService.fetchTransactions().subscribe((data: ITransaction[]) => {
      this.contentLoaded++;
      this.transactions = data;
    }, (error: IResponse) => {
      this.contentLoaded++;

      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
      this.router.navigate(["**"]);
    })
  }

  fetchPartnerTransactions() {
    this.contentLoaded--;
    this.resetOrderFilters();

    if (this.selectedPartner !== null) {
      this.transactionService.fetchPartnerTransactions(this.selectedPartner).subscribe((transactions: ITransaction[]) => {
        this.contentLoaded++;
        this.transactions = transactions;
      }, (error: IResponse) => {
        this.contentLoaded++;

        window.alert(error.message || "Unable to connect to third party services, please refresh the page");
        this.router.navigate(["**"]);
      })
    } else {
      this.contentLoaded++;
    }
  }

  fetchPartners() {
    this.contentLoaded--;

    this.partnerService.getPartners().subscribe((partners: IReckoPartner[]) => {
      this.contentLoaded++;
      this.partners = partners;
    }, (error: IResponse) => {
      this.contentLoaded++;

      window.alert(error.message || "Unable to connect to third party services, please refresh the page");
      this.router.navigate(["**"]);
    })
  }

  changeHolderOrder() {
    this.holderAscending = !this.holderAscending;
    this.transactions.sort((a, b) => {
      return (this.holderAscending)
        ? a.holderName.toLowerCase().localeCompare(b.holderName.toLowerCase())
        : b.holderName.toLowerCase().localeCompare(a.holderName.toLowerCase());
    })
  }

  changeReceiverOrder() {
    this.receiverAscending = !this.receiverAscending;
    this.transactions.sort((a, b) => {
      if (a.receiver == null) {
        return 1;
      }

      if (b.receiver == null) {
        return -1;
      }

      return (this.receiverAscending)
        ? a.receiver.toLowerCase().localeCompare(b.receiver.toLowerCase())
        : b.receiver.toLowerCase().localeCompare(a.receiver.toLowerCase());
    })
  }

  changeTypeOrder() {
    this.typeAscending = !this.typeAscending;
    this.transactions.sort((a, b) => {
      return (this.typeAscending)
        ? a.type.toLowerCase().localeCompare(b.type.toLowerCase())
        : b.type.toLowerCase().localeCompare(a.type.toLowerCase());
    })
  }

  changeAmountOrder() {
    this.amountAscending = !this.amountAscending;
    this.transactions.sort((a, b) => {
      return (this.amountAscending) ? a.amount - b.amount : b.amount - a.amount;
    })
  }

  private compareConsumerDate(a: string, b: string): number {
    if (a !== b) {
      const aElements: number[] = a.split("-").map(d => parseInt(d));
      const bElements: number[] = b.split("-").map(d => parseInt(d));

      console.log(aElements, bElements);

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
    this.transactions.sort((a, b) => {
      return (this.dateAscending) ? this.compareConsumerDate(a.date.toString(), b.date.toString())
        : this.compareConsumerDate(b.date.toString(), a.date.toString());
    })
  }
}
