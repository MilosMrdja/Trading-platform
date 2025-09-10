import { Injectable, Inject } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {AccountStatusResponse} from '../../../shared/models/account-status-response';
import {AccountStatusCreateRequest} from '../../../shared/models/account-status-create-request';
import {API_BASE_URL} from '../../../app.token';

@Injectable({
  providedIn: 'root'
})
export class AccountStatusService {


  constructor(@Inject(API_BASE_URL) private apiUrl: string, private http: HttpClient) { }
  private baseUrl = '/api/account-status';
  getAll(page: number = 0): Observable<ResponseDTO<AccountStatusResponse>> {
    let params = new HttpParams()
      .set('page', page);
    return this.http.get<ResponseDTO<AccountStatusResponse>>(`${this.apiUrl}${this.baseUrl}`, { params });
  }

  getById(id: number): Observable<AccountStatusResponse> {
    return this.http.get<AccountStatusResponse>(`${this.apiUrl}${this.baseUrl}/${id}`);
  }

  create(request: AccountStatusCreateRequest): Observable<AccountStatusResponse> {
    return this.http.post<AccountStatusResponse>(`${this.apiUrl}${this.baseUrl}`, request);
  }
}
