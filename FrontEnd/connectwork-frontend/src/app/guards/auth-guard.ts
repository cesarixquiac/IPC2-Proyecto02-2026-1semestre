import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  if (authService.estaAutenticado()) {
    return true; // Déjalo pasar
  } else {
    router.navigate(['/login']); // Patéalo al Login
    return false;
  }
};