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

  obtenerDetalleEntrega(idProyecto: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${idProyecto}/entregas`);
  }

  aprobarEntrega(idProyecto: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/entregas/aprobar`, {});
  }

  rechazarEntrega(idProyecto: number, motivo: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/entregas/rechazar`, { motivo });
  }

  cancelarContrato(idProyecto: number, motivo: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/cancelar`, { motivo });
  }

  aprobarEntregaConCalificacion(idProyecto: number, calificacion: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/entregas/aprobar`, calificacion);
  }

  editarProyecto(idProyecto: number, datos: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/editar`, datos);
  }

  eliminarProyecto(idProyecto: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${idProyecto}/eliminar`, {});
  }

  enviarPropuesta(propuesta: any): Observable<any> {
    
    return this.http.post(`${environment.apiUrl}/propuestas/enviar`, propuesta);
  }

  // Obtener los contratos activos del freelancer
  obtenerMisContratos(): Observable<any> {
    return this.http.get(`${this.apiUrl}/mis-contratos`);
  }

  // Subir la entrega del trabajo
  subirEntrega(entrega: any): Observable<any> {
    // Apunta al endpoint que ya tienes diseñado: /api/entregas/subir
    return this.http.post(`${environment.apiUrl}/entregas/subir`, entrega);
  }


}