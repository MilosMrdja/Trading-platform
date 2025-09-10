import { Injectable, Inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerServiceService} from '../errors/error-handler-service.service';
import {catchError, Observable} from 'rxjs';
import {ResponseDTO} from '../../../shared/models/ResponseDTO';
import {InstrumentResponse} from '../../../shared/models/instrument-response';
import {InstrumentRequest} from '../../../shared/models/instrument-request';
import {API_BASE_URL} from '../../../app.token';

@Injectable({
  providedIn: 'root'
})
export class InstrumentsService {

  private readonly baseUrl = '/api/instruments';

  constructor(@Inject(API_BASE_URL) private apiUrl: string, private http: HttpClient, private errorHandler: ErrorHandlerServiceService) {}

  getAll(page: number): Observable<ResponseDTO<InstrumentResponse>> {
    return this.http
      .get<ResponseDTO<InstrumentResponse>>(`${this.apiUrl}${this.baseUrl}?page=${page}`)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'getAll Instruments')));
  }

  createInstrument(request: InstrumentRequest): Observable<InstrumentResponse> {
    return this.http
      .post<InstrumentResponse>(`${this.apiUrl}${this.baseUrl}`, request)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'create Instrument')));
  }

  updateInstrument(id: number, request: InstrumentRequest): Observable<InstrumentResponse> {
    return this.http
      .put<InstrumentResponse>(`${this.apiUrl}${this.baseUrl}/${id}`, request)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'update Instrument')));
  }

  deleteInstrument(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.apiUrl}${this.baseUrl}/${id}`)
      .pipe(catchError(err => this.errorHandler.handleError(err, 'delete Instrument')));
  }
}
