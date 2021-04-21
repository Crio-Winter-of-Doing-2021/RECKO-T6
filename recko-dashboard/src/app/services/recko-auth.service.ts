import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from '../../environments/environment';
import { ServiceErrorHandler } from './utils/service-error-handler';

import { IChangePassword } from '../models/change-password.model';
import { IReckoOperator } from '../models/recko-operator.model';
import { StorageKey } from '../models/storage-key.model';
import { ICompanyHandler } from '../models/company-handler.model';
import { IResponse } from '../models/response.model';

@Injectable({
    providedIn: 'root'
})
export class ReckoAuthService {

    private readonly baseApiUrl = `${environment.apiUrl}/api/auth`;

    private readonly httpOptions = {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
    };

    constructor(private http: HttpClient, private handler: ServiceErrorHandler) { }

    get authenticatedUsername(): string {
        return localStorage.getItem(StorageKey.username);
    }

    get isAuthenticated(): boolean {
        return localStorage.getItem(StorageKey.username) !== null && localStorage.getItem(StorageKey.isAdmin) !== null;
    }

    get isAdminAuthenticated(): boolean {
        const username = localStorage.getItem(StorageKey.username) || null;
        const isAdmin: boolean = localStorage.getItem(StorageKey.isAdmin) == "true";
        return username !== null && isAdmin;
    }

    get isModAuthenticated(): boolean {
        const username: string = localStorage.getItem(StorageKey.username) || null;
        const isModerator: boolean = localStorage.getItem(StorageKey.isAdmin) == "false";
        return username !== null && isModerator;
    }

    setTokens(operator: IReckoOperator, isAdmin: boolean = true) {
        localStorage.setItem(StorageKey.username, operator.username);
        localStorage.setItem(StorageKey.isAdmin, isAdmin.toString());
        localStorage.setItem(StorageKey.company, operator.company.id);
    }

    logout() {
        localStorage.clear();
    }

    loginAdmin(admin: IReckoOperator): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/adminLogin`;
        return this.http.post<IReckoOperator>(url, admin, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    loginModerator(moderator: IReckoOperator): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/moderatorLogin`;
        return this.http.post<IReckoOperator>(url, moderator, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    changePasswordAdmin(adminPass: IChangePassword): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/adminChangePassword`;
        return this.http.patch<IReckoOperator>(url, adminPass, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    changePasswordModerator(modPass: IChangePassword): Observable<IReckoOperator> {
        const url = `${this.baseApiUrl}/moderatorChangePassword`;
        return this.http.patch<IReckoOperator>(url, modPass, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    forgotPasswordAdmin(companyHandler: ICompanyHandler): Observable<IResponse> {
        const url = `${this.baseApiUrl}/adminForgotPassword`;
        return this.http.post<IResponse>(url, companyHandler, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    forgotPasswordModerator(companyHandler: ICompanyHandler): Observable<IResponse> {
        const url = `${this.baseApiUrl}/moderatorForgotPassword`;
        return this.http.post<IResponse>(url, companyHandler, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }
}