import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-fallback',
  templateUrl: './fallback.component.html',
  styleUrls: ['./fallback.component.css']
})
export class FallbackComponent implements OnInit {

  message: string;
  private readonly defaultMessage: string = "Something went wrong, please try again";

  constructor(private router: Router) {
    const suppliedMessage: boolean = this.router.getCurrentNavigation().extras.queryParams.message;
    this.message = suppliedMessage ? suppliedMessage.toString() : this.defaultMessage;
  }

  ngOnInit(): void {
  }

  navigateToHome() {
    this.router.navigate(["home"]);
  }
}
