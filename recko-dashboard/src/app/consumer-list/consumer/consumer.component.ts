import { Component, Input, OnInit, Output, TemplateRef, ViewChild, ViewContainerRef, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

import { ReckoConsumerService } from '../../services/recko-consumer.service';
import { ReckoAuthService } from '../../services/recko-auth.service';

import { IReckoConsumer } from '../../models/recko-consumer.model';
import { IResponse } from '../../models/response.model';

@Component({
  selector: 'app-consumer',
  templateUrl: './consumer.component.html',
  styleUrls: ['./consumer.component.css']
})
export class ConsumerComponent implements OnInit {

  @Input("consumer") consumer: IReckoConsumer;
  @Output("delete") delEmitter = new EventEmitter<IReckoConsumer>();

  @ViewChild("consumerRef", { static: true }) consumerRef: TemplateRef<any>;

  isLoading: boolean = false;

  constructor(private viewContainerRef: ViewContainerRef,
    private router: Router,
    private consumerService: ReckoConsumerService,
    private authService: ReckoAuthService) { }

  ngOnInit(): void {
    this.viewContainerRef.createEmbeddedView(this.consumerRef);
  }

  isAdminAuthenticated(): boolean {
    return this.authService.isAdminAuthenticated;
  }

  deleteConsumer() {
    const toDelete: boolean = window.confirm("Are You Sure To Delete or Not ?");
    if (toDelete) {
      this.isLoading = true;

      this.consumerService.deleteConsumer(this.consumer).subscribe((response: IResponse) => {
        this.isLoading = false;
        window.alert(response.message);
        this.delEmitter.emit(this.consumer);
        this.router.navigate(["consumer-list"]);
      })
    }
  }

  redirectUpdate() {
    this.router.navigate(["consumer-form"], { queryParams: { consumer: this.consumer } });
  }

}
