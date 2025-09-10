import { Injectable, Inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerServiceService} from '../errors/error-handler-service.service';
import {catchError, Observable} from 'rxjs';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {UserResponse} from '../../../shared/models/user-response';
import {UserRequest} from '../../../shared/models/user-request';
import {API_BASE_URL} from '../../../app.token';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private readonly baseUrl = '/api/users';

  constructor(@Inject(API_BASE_URL) private apiUrl:string, private http: HttpClient, private errorHandler: ErrorHandlerServiceService) {}

  getAll(page: number): Observable<ResponseDTO<UserResponse>> {
    return this.http.get<ResponseDTO<UserResponse>>(`${this.apiUrl}${this.baseUrl}?page=${page}`)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'getAll Users')));
  }

  createManager(request: UserRequest) {
    return this.http.post<UserResponse>(`${this.apiUrl}${this.baseUrl}/manager`, request)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'create User manager')));
  }

  createBroker(request: UserRequest) {
    return this.http.post<UserResponse>(`${this.apiUrl}${this.baseUrl}/broker`, request)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'create User broker')));
  }

  updateUser(id: number, request: UserRequest) {
    return this.http.put<UserResponse>(`${this.apiUrl}${this.baseUrl}/${id}`, request)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'update User')));
  }
}
