import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { catchError, shareReplay, tap } from 'rxjs/operators';
import {Role} from '../../shared/enums/role';
import {UserResponse} from '../../shared/models/user-response';
import {LoginResponse} from '../../shared/models/login-response';
import {ErrorHandlerServiceService} from './errors/error-handler-service.service';
import {API_BASE_URL} from '../../app.token';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = '/api/auth';
  private me$?: Observable<UserResponse>;

  constructor(@Inject(API_BASE_URL) private apiUrl: string, private http: HttpClient, private errorHandler: ErrorHandlerServiceService) {}

  login(email: string, password: string): Observable<LoginResponse> {
    console.log(`${this.apiUrl}${this.baseUrl}/login`);
    return this.http
      .post<LoginResponse>(`${this.apiUrl}${this.baseUrl}/login`, { email, password })
      .pipe(catchError(this.handleError));
  }

  signup(name: string, email: string, password: string, role: Role): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}${this.baseUrl}/signup`, { name, email, password, role })
      .pipe(catchError(this.handleError));
  }


  logout(): Observable<void> {
    return this.http
      .post<void>(`${this.apiUrl}${this.baseUrl}/logout`, {})
      .pipe(
        tap(() => { this.me$ = undefined; }),
        catchError(this.handleError)
      );
  }

  me(): Observable<UserResponse> {
    if (!this.me$) {
      this.me$ = this.http.get<UserResponse>(`${this.apiUrl}${this.baseUrl}/me`).pipe(
        shareReplay(1),
        catchError(err => {
          this.me$ = undefined;
          return this.handleError(err);
        })
      );
    }
    return this.me$;
  }

  private handleError(err: HttpErrorResponse) {
    let message = 'Something went wrong, please try again.';
    if (err.error && typeof err.error === 'object' && 'message' in err.error) {
      message = (err.error as any).message || message;
    } else if (typeof err.error === 'string') {
      message = err.error;
    }
    return throwError(() => new Error(message));
  }
}
