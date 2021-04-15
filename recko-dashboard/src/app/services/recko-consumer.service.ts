import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { environment } from "../../environments/environment";
import { ServiceErrorHandler } from './utils/service-error-handler';

import { IReckoConsumer } from '../models/recko-consumer.model';
import { IResponse } from '../models/response.model';
import { StorageKey } from "../models/storage-key.model";

@Injectable({
    providedIn: 'root'
})
export class ReckoConsumerService {

    private readonly baseApiUrl = `${environment.apiUrl}/api/accounts`;
    private readonly httpOptions = {
        headers: new HttpHeaders({ "Content-Type": "application/json" })
    };

    constructor(private http: HttpClient, private handler: ServiceErrorHandler) { }

    getConsumers(): Observable<IReckoConsumer[]> {
        const url = `${this.baseApiUrl}/${localStorage.getItem(StorageKey.company)}`;
        return this.http.get<IReckoConsumer[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    getPartnerConsumers(partnerName: string): Observable<IReckoConsumer[]> {
        const url = `${this.baseApiUrl}/${localStorage.getItem(StorageKey.company)}/${partnerName}`;
        return this.http.get<IReckoConsumer[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    getConsumerTypes(partnerName: string): Observable<string[]> {
        const url = `${this.baseApiUrl}/${partnerName}/types`;
        return this.http.get<string[]>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    addConsumer(consumer: IReckoConsumer): Observable<IReckoConsumer> {
        const url = `${this.baseApiUrl}/create`;
        return this.http.post<IReckoConsumer>(url, consumer, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    updateConsumer(consumer: IReckoConsumer): Observable<IReckoConsumer> {
        const url = `${this.baseApiUrl}/update`;
        return this.http.patch<IReckoConsumer>(url, consumer, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    deleteConsumer(consumer: IReckoConsumer): Observable<IResponse> {
        const url = `${this.baseApiUrl}/delete`;
        return this.http.patch<IResponse>(url, consumer, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }
}