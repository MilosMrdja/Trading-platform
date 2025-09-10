import { Injectable, Inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {AccountResponse} from '../../../shared/models/account-response';
import {ErrorHandlerServiceService} from '../errors/error-handler-service.service';
import {AccountRequest} from '../../../shared/models/account-request';
import {API_BASE_URL} from '../../../app.token';

@Injectable({
  providedIn: 'root'
})
export class AccountServiceService {
  private readonly baseUrl = '/api/accounts';

  constructor(@Inject(API_BASE_URL) private apiUrl:string, private  http: HttpClient, private errorHandler: ErrorHandlerServiceService) { }

  getAll(page:number): Observable<ResponseDTO<AccountResponse>> {
    return this.http.get<ResponseDTO<AccountResponse>>(`${this.apiUrl}${this.baseUrl}?page=${page}`)
      .pipe(
        catchError(err => this.errorHandler.handleError(err, 'getAll Accounts'))
      );
  }
  createAccount(request: AccountRequest): Observable<AccountResponse> {
    return this.http.post<AccountResponse>(`${this.apiUrl}${this.baseUrl}`, request)
      .pipe(
        catchError(err => this.errorHandler.handleError(err, 'create Account'))
      );
  }

  updateAccount(id: number, request: AccountRequest): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(`${this.apiUrl}${this.baseUrl}/${id}`, request)
      .pipe(
        catchError(err => this.errorHandler.handleError(err, `update Account id=${id}`))
      );
  }

  deleteAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${this.baseUrl}/${id}`)
      .pipe(
        catchError(err => this.errorHandler.handleError(err, `delete Account id=${id}`))
      );
  }
}
