import { Injectable, Inject } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {TradeResponse} from '../../../shared/models/trade-response';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {TradeRequest} from '../../../shared/models/trade-request';
import {TradeFilter} from '../../../shared/models/trade-filter';
import {TradeExerciseRequest} from '../../../shared/models/trade-exercise-request';
import {ErrorHandlerServiceService} from '../errors/error-handler-service.service';
import {API_BASE_URL} from '../../../app.token';

@Injectable({
  providedIn: 'root'
})
export class TradeServiceService {
  private baseUrl = '/api/trades';


  constructor(@Inject(API_BASE_URL) private apiUrl: string, private http: HttpClient, private errorHandler: ErrorHandlerServiceService) {}



  getAll(page: number, filters: TradeFilter): Observable<ResponseDTO<TradeResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())// default sort

    if (filters.direction) {
      params = params.set('direction', filters.direction);
    }
    if (filters.status) {
      params = params.set('status', filters.status);
    }
    if (filters.price !== null && filters.price !== undefined) {
      params = params.set('price', filters.price.toString());
    }
    return this.http.get<ResponseDTO<TradeResponse>>(`${this.apiUrl}${this.baseUrl}`, {params})
      .pipe(catchError(err => this.errorHandler.handleError(err, 'getAll trades')));
  }

  createTrade(req: TradeRequest): Observable<TradeResponse> {
    return this.http.post<TradeResponse>(`${this.apiUrl}${this.baseUrl}`, req)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'Create Trade')));
  }

  updateTrade(id: number, req: TradeRequest): Observable<TradeResponse> {
    return this.http.put<TradeResponse>(`${this.apiUrl}${this.baseUrl}/${id}`, req)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'Update Trade')));
  }

  deleteTrade(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'Delete Trade')));
  }

  exerciseTrade(id: number, req: TradeExerciseRequest): Observable<TradeResponse> {
    return this.http.put<TradeResponse>(`${this.apiUrl}${this.baseUrl}/${id}/exercise`, req)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'Exercise Trade')));
  }
}
