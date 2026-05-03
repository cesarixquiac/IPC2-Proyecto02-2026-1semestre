import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) { }

  login(credenciales: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credenciales).pipe(
      tap((res: any) => {
        if (res && res.token) {
          this.guardarToken(res.token);
        }
      })
    );
  }

  registrar(usuario: any): Observable<any> {
   
    return this.http.post(`${this.apiUrl}/registro`, usuario); 
  }

  guardarToken(token: string) {
    localStorage.setItem('token', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('token');
  }

  cerrarSesion() {
    localStorage.removeItem('token');
  }

  obtenerDatosUsuario(): any {
    const token = this.obtenerToken();
    if (token) {
      try {
        return jwtDecode(token);
      } catch (e) {
        return null;
      }
    }
    return null;
  }

  estaAutenticado(): boolean {
    return !!this.obtenerToken();
  }
}