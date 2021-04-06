import { Component, OnInit, Input, ViewChild, TemplateRef, ViewContainerRef } from '@angular/core';

import { ITransaction } from '../../models/transaction.model';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  @Input("tran") transaction: ITransaction;
  @ViewChild("tranTemplate", { static: true }) tranTemplate: TemplateRef<any>;

  constructor(private viewContainerRef: ViewContainerRef) { }

  ngOnInit(): void {
    this.viewContainerRef.createEmbeddedView(this.tranTemplate);
  }

}
