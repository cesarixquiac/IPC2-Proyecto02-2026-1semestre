import { CanActivateFn, Router } from '@angular/router';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Auth } from '../services/auth'; // Asegúrate de que la ruta sea correcta

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);
  const platformId = inject(PLATFORM_ID);

  // 1. Evitamos el error de SSR asegurándonos de que estamos en el navegador
  if (isPlatformBrowser(platformId)) {
    
    // 2. ¿Tiene token?
    if (authService.estaAutenticado()) {
      
      // 3. Obtenemos los datos decodificados del JWT
      const usuario = authService.obtenerDatosUsuario(); 
      const urlDestino = state.url;

      // 4. Protección estricta por Roles
      // (Asegúrate de que 'usuario.rol' coincida con cómo lo llamas en tu base de datos)
      if (urlDestino.includes('/admin') && usuario?.rol !== 'ADMIN') {
        router.navigate(['/login']);
        return false;
      }
      if (urlDestino.includes('/cliente') && usuario?.rol !== 'CLIENTE') {
        router.navigate(['/login']);
        return false;
      }
      if (urlDestino.includes('/freelancer') && usuario?.rol !== 'FREELANCER') {
        router.navigate(['/login']);
        return false;
      }

      // Si todo está perfecto, lo dejamos pasar
      return true; 
    }
  }

  // Si no hay token, o si el servidor está intentando leerlo, lo pateamos al login
  router.navigate(['/login']);
  return false;
};