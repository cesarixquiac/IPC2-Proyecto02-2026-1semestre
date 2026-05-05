import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FreelancerService {
  private apiUrl = 'http://localhost:8080/ConnectWork/api';

  constructor(private http: HttpClient) { }

  // 1. Obtener catálogo de habilidades para mostrar en los checkboxes
  obtenerHabilidades(): Observable<any> {
    return this.http.get(`${this.apiUrl}/habilidades`);
  }

  // 2. Enviar el perfil completo
  completarPerfil(perfil: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/freelancers/perfil`, perfil);
  }

  verificarPerfilEstado(): Observable<any> {
    return this.http.get(`${this.apiUrl}/freelancers/perfil/estado`);
  }

  obtenerSaldo(): Observable<any> {
    return this.http.get(`${this.apiUrl}/freelancers/perfil/saldo`);
  }

  // Obtener historial de ganancias del freelancer
  obtenerHistorialGanancias(): Observable<any> {
    
    return this.http.get(`${this.apiUrl}/freelancers/historial-ganancias`);
  }
  
}