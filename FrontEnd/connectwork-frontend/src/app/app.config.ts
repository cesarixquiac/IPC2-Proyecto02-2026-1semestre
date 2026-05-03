import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { jwtInterceptor } from './interceptors/jwt-interceptor'; // Importamos el interceptor

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    // Agregamos withInterceptors y le pasamos nuestra función
    provideHttpClient(withInterceptors([jwtInterceptor])) 
  ]
};