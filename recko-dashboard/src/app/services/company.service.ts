import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from "rxjs/operators";

import { environment } from '../../environments/environment';
import { ServiceErrorHandler } from './utils/service-error-handler';

import { ICompanyRegister } from '../models/company-register.model';
import { ICompany } from '../models/company.model';
import { IReckoOperatorRegister } from '../models/operator-register.model';
import { IReckoOperator } from '../models/recko-operator.model';
import { ICompanyCredentialRegister } from '../models/company-credential-register.model';
import { ICompanyCredential } from '../models/company-credential.model';
import { StorageKey } from "../models/storage-key.model";

@Injectable({
    "providedIn": "root"
})
export class CompanyService {

    private readonly baseApiUrl = `${environment.apiUrl}/api/companies`;
    private readonly httpOptions = {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
    };

    constructor(private http: HttpClient, private handler: ServiceErrorHandler) { }

    registerCompany(newCompany: ICompanyRegister): Observable<ICompany> {
        const url = `${this.baseApiUrl}/register/company`;
        return this.http.post<ICompany>(url, newCompany, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    registerAdmin(newAdmin: IReckoOperatorRegister): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/register/admin`;
        return this.http.post<IReckoOperator>(url, newAdmin, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    registerModerator(newModerator: IReckoOperatorRegister): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/register/moderator`;
        return this.http.post<IReckoOperator>(url, newModerator, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    fetchCompanyHandlerRoles(): Observable<string[]> {
        const url = `${this.baseApiUrl}/roles`;
        return this.http.get<string[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    fetchCompanyCredentials(partner: string): Observable<ICompanyCredential[]> {
        const url = `${this.baseApiUrl}/credentials/${localStorage.getItem(StorageKey.company)}/${partner}`;
        return this.http.get<ICompanyCredential[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    registerCompanyCredential(newCredential: ICompanyCredentialRegister, partnerName: string): Observable<ICompanyCredential> {
        const url = `${this.baseApiUrl}/register/credential/${partnerName}`;
        return this.http.post<ICompanyCredential>(url, newCredential, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }
}