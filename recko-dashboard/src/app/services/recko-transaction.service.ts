import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from "rxjs";
import { catchError } from "rxjs/operators";

import { ServiceErrorHandler } from './utils/service-error-handler';

import { environment } from '../../environments/environment';
import { ITransaction } from '../models/transaction.model';
import { StorageKey } from '../models/storage-key.model';

@Injectable({
    providedIn: 'root'
})
export class ReckoTransactionService {

    private readonly baseApiUrl = `${environment.apiUrl}/api/transactions`;

    private readonly httpOptions = {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
    };

    constructor(private http: HttpClient, private handler: ServiceErrorHandler) { }

    fetchTransactions(): Observable<ITransaction[]> {
        const url = `${this.baseApiUrl}/${localStorage.getItem(StorageKey.company)}`;
        return this.http.get<ITransaction[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    fetchPartnerTransactions(partner: string): Observable<ITransaction[]> {
        const url = `${this.baseApiUrl}/${localStorage.getItem(StorageKey.company)}/${partner}`;
        return this.http.get<ITransaction[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }
}