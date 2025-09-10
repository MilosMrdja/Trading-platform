import { CanActivateFn, Router, ActivatedRouteSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, map, of } from 'rxjs';
import {UserResponse} from '../../shared/models/user-response';

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  const allowedRoles = (route.data['roles'] as string[] | undefined)?.map(r => r.toLowerCase());

  return auth.me().pipe(
    map((user: UserResponse): boolean | UrlTree => {

      const userRole = String((user as any).role || '').toLowerCase().replace(/^role_/, '');
      if (!allowedRoles || allowedRoles.includes(userRole)) {
        return true;
      }
      return router.createUrlTree(['/']);
    }),
    catchError(() => {
      return of(router.createUrlTree(['/login']));
    })
  );
};
