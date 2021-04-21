import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home.component';

import { ConsumerListComponent } from './consumer-list/consumer-list.component';
import { ConsumerComponent } from './consumer-list/consumer/consumer.component';
import { ConsumerFormComponent } from './consumer-list/consumer-form/consumer-form.component';

import { LoginComponent } from './authentication/login/login.component';
import { ChangePasswordComponent } from './authentication/change-password/change-password.component';
import { ForgotPasswordComponent } from './authentication/forgot-password/forgot-password.component';

import { FallbackComponent } from './helpers/fallback/fallback.component';

import { TransactionListComponent } from "./transaction-list/transaction-list.component";

import { CompanyRegisterComponent } from './company/company-register/company-register.component';
import { CompanyHandlerRegisterComponent } from './company/company-handler-register/company-handler-register.component';
import { CompanyCredentialRegisterComponent } from './company/company-credential-register/company-credential-register.component';

import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: "home", component: HomeComponent },
  { path: "", redirectTo: "home", pathMatch: "full" },

  { path: "consumer-list", component: ConsumerListComponent, canActivate: [AuthGuard] },
  { path: "consumer/:id", component: ConsumerComponent, canActivate: [AuthGuard] },
  { path: "consumer-form", component: ConsumerFormComponent, canActivate: [AuthGuard] },

  { path: "transaction-list", component: TransactionListComponent, canActivate: [AuthGuard] },

  { path: "company-register", component: CompanyRegisterComponent },
  { path: "company-handler-register", component: CompanyHandlerRegisterComponent },
  { path: "company-credential-register", component: CompanyCredentialRegisterComponent },

  { path: "change-password", component: ChangePasswordComponent, canActivate: [AuthGuard] },
  { path: "forgot-password", component: ForgotPasswordComponent },
  { path: "login", component: LoginComponent },

  { path: "**", component: FallbackComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
