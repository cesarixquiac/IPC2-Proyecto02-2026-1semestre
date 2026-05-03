import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Auth } from '../services/auth';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(Auth);
  const token = authService.obtenerToken();

  // Si hay token, clonamos la petición y le agregamos el header
  if (token) {
    const peticionClonada = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(peticionClonada); // Enviamos la petición modificada
  }

  // Si no hay token (ej. al hacer login), enviamos la petición tal cual
  return next(req);
};