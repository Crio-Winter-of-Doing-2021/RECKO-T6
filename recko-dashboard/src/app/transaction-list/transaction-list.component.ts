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
  storedTransactions: ITransaction[] = [];
  partners: IReckoPartner[] = [];

  selectedPartner: string = null;
  nameFilter: string = null;
  dateFilter: string = null;

  contentLoaded: number = 2;

  holderAscending = null;
  typeAscending = null;
  amountAscending = null;
  dateAscending = false;

  readonly itemsPerPage = 15;
  currentPage: number = 1;
  readonly maxPaginationSize = 100;

  constructor(private transactionService: ReckoTransactionService,
    private partnerService: ReckoPartnerService,
    private router: Router) { }

  ngOnInit(): void {
    this.fetchTransactions();
    this.fetchPartners();
  }

  fetchTransactions() {
    this.contentLoaded--;
    this.resetOrderFilters();

    this.transactionService.fetchTransactions().subscribe((data: ITransaction[]) => {
      this.contentLoaded++;
      this.transactions = data;
      this.storedTransactions = data;
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
        this.storedTransactions = transactions;

        if (this.nameFilter !== null || this.dateFilter !== null) {
          this.applySearchFilters();
        }
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

  applySearchFilters() {

    let filteredTransactions: ITransaction[] = [];

    if (this.nameFilter !== null && this.nameFilter.length > 0) {
      filteredTransactions = this.storedTransactions
        .filter((con) => con.holder.toLowerCase().indexOf(this.nameFilter.toLowerCase()) > -1);
    }

    if (this.dateFilter !== null && this.dateFilter.length > 0) {
      if (filteredTransactions.length === 0) {
        filteredTransactions = this.storedTransactions
          .filter((con) => con.date.toString() === this.dateFilter);
      } else {
        filteredTransactions = filteredTransactions
          .filter((con) => con.date.toString() === this.dateFilter);
      }
    }

    this.transactions = filteredTransactions;
  }

  resetSearchFilters() {
    this.selectedPartner = null;
    this.fetchTransactions();
  }

  disableSearchFilters(): boolean {
    return this.nameFilter === null && this.dateFilter === null;
  }

  private resetOrderFilters() {
    this.holderAscending = this.amountAscending = this.typeAscending = null;
    this.dateAscending = false;
  }

  changeHolderOrder() {
    this.holderAscending = !this.holderAscending;
    this.transactions.sort((a, b) => {
      return (this.holderAscending)
        ? a.holder.toLowerCase().localeCompare(b.holder.toLowerCase())
        : b.holder.toLowerCase().localeCompare(a.holder.toLowerCase());
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
