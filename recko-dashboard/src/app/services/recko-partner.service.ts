import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { ServiceErrorHandler } from './utils/service-error-handler';
import { IReckoPartner } from '../models/recko-partner.model';

@Injectable({
    providedIn: 'root'
})
export class ReckoPartnerService {

    private readonly baseApiUrl: string = `${environment.apiUrl}/api/services`;

    private readonly httpOptions = {
        headers: new HttpHeaders({ "Content-Type": "applicaion/json" })
    };

    constructor(private http: HttpClient, private handler: ServiceErrorHandler) { }

    getPartners(): Observable<IReckoPartner[]> {
        return this.http.get<IReckoPartner[]>(this.baseApiUrl, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }

    getPartner(partnerName: string): Observable<IReckoPartner> {
        const url = `${this.baseApiUrl}/${partnerName}`;
        return this.http.get<IReckoPartner>(url, this.httpOptions).pipe(catchError(this.handler.errorHandler));
    }
}