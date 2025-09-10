import { Injectable } from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerServiceService {

  constructor() {}

  handleError(error: HttpErrorResponse, context?: string) {
    let userFriendlyMessage = '';

    if (error.error && error.error.message) {
      userFriendlyMessage = `${error.error.message}`;
    } else {
      userFriendlyMessage = `Unexpected error occurred`;
    }

    console.error('HTTP Error:', {
      context,
      status: error.status,
      message: error.message,
      backendBody: error.error
    });

    // TODO toast
    return throwError(() => new Error(userFriendlyMessage));
  }
}
