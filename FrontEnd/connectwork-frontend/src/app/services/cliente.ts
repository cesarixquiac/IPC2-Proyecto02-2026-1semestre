import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  
  private apiUrl = `${environment.apiUrl}/clientes`;

  constructor(private http: HttpClient) { }

  completarPerfil(perfil: any): Observable<any> {
 
    return this.http.post(`${this.apiUrl}/perfil`, perfil);
  }

  verificarPerfil(): Observable<any> {
    return this.http.get(`${this.apiUrl}/perfil/existe`);
  }

  recargarSaldo(monto: number): Observable<any> {
 
  return this.http.post(`${this.apiUrl}/recarga`, { monto });
}

  obtenerPerfil(): Observable<any> {
    return this.http.get(`${this.apiUrl}/perfil`);
  }

  // Obtener historial de recargas del cliente
  obtenerHistorialRecargas(): Observable<any> {
    return this.http.get(`${this.apiUrl}/historial-recargas`);
  }

  // --- MÉTODOS DE REPORTES ---
  reporteProyectos(idCliente: number, inicio: string, fin: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/proyectos?idCliente=${idCliente}&inicio=${inicio}&fin=${fin}`);
  }

  reporteRecargas(idCliente: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/recargas?idCliente=${idCliente}`);
  }

  reporteGastos(idCliente: number, inicio: string, fin: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/gastos?idCliente=${idCliente}&inicio=${inicio}&fin=${fin}`);
  }

  solicitarCategoria(idUsuario: number, nombre: string, descripcion: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/solicitar-categoria`, { idUsuario, nombre, descripcion });
  }

}