import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';
import {API_BASE_URL} from './app.token';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import {provideAnimations} from '@angular/platform-browser/animations';
import {environment} from '../../enviroments/environment';

const credentialsInterceptor = (req: any, next: any) => {
  // Ensure all requests include credentials (cookies) for HttpOnly JWT handling
  const withCreds = req.clone({ withCredentials: true });
  return next(withCreds);
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(),
    provideHttpClient(withInterceptors([credentialsInterceptor])),
    { provide: API_BASE_URL, useValue: environment.API_BASE_URL || 'http://localhost:8080' }
  ]
};
