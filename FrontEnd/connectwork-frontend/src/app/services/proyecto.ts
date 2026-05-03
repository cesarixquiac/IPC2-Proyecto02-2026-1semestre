import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {
  private apiUrl = `${environment.apiUrl}/proyectos`; 
  private categoriasUrl = `${environment.apiUrl}/categorias`; 
  private habilidadesUrl = `${environment.apiUrl}/habilidades`; 

  constructor(private http: HttpClient) { }

  publicarProyecto(proyecto: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/publicar`, proyecto);
  }

  // Nuevos métodos para traer los datos reales
  obtenerCategorias(): Observable<any> {
    return this.http.get(this.categoriasUrl);
  }

  obtenerHabilidades(): Observable<any> {
    return this.http.get(this.habilidadesUrl);
  }

  obtenerProyectosDisponibles(): Observable<any> {
    return this.http.get(`${this.apiUrl}/disponibles`);
  }

  obtenerMisProyectos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/mis-proyectos`);
  }

  obtenerPropuestasProyecto(idProyecto: number): Observable<any> {
    
    return this.http.get(`${this.apiUrl}/${idProyecto}/propuestas`);
  }

  aceptarPropuesta(idProyecto: number, idPropuesta: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/propuestas/aceptar`, { idProyecto, idPropuesta });
  }

  rechazarPropuesta(idPropuesta: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/propuestas/rechazar`, { idPropuesta });
  }

}