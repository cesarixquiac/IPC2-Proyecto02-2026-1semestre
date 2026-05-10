import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  // Ajusta la URL base según tu proyecto
  private apiUrl = 'http://localhost:8080/ConnectWork/api/admin';
  private categoriasUrl = 'http://localhost:8080/ConnectWork/api/categorias'; // Ajusta esto a tu endpoint de categorías

  constructor(private http: HttpClient) { }

  obtenerSolicitudes(): Observable<any> {
    return this.http.get(`${this.apiUrl}/solicitudes`);
  }

  procesarSolicitud(idSolicitud: number, accion: string, idCategoria?: number): Observable<any> {
    const payload = { idSolicitud, accion, idCategoria };
    return this.http.post(`${this.apiUrl}/procesar-solicitud`, payload);
  }

  // Necesitamos las categorías para que el admin asigne las nuevas habilidades
  obtenerCategorias(): Observable<any> {
    return this.http.get(this.categoriasUrl); 
  }

  // --- MÉTODOS DE COMISIÓN ---
  obtenerComisionActual(): Observable<any> {
    return this.http.get(`${this.apiUrl}/comision-actual`);
  }

  actualizarComision(porcentaje: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/actualizar-comision`, { porcentaje });
  }

  // --- MÉTODOS DE GESTIÓN DE CATEGORÍAS (CRUD) ---
  obtenerCategoriasAdmin(): Observable<any> {
    return this.http.get(`${this.apiUrl}/categorias`);
  }

  crearCategoria(nombre: string, descripcion: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/crear-categoria`, { nombre, descripcion });
  }

  editarCategoria(idCategoria: number, nombre: string, descripcion: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/editar-categoria`, { idCategoria, nombre, descripcion });
  }

  cambiarEstadoCategoria(idCategoria: number, estado: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/estado-categoria`, { idCategoria, estado });
  }

  // --- MÉTODOS DE REPORTES ---
  reporteComisiones(): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/comisiones`);
  }

  reporteTopFreelancers(inicio: string, fin: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/top-freelancers?inicio=${inicio}&fin=${fin}`);
  }

  reporteTopCategorias(inicio: string, fin: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/top-categorias?inicio=${inicio}&fin=${fin}`);
  }

  reporteIngresos(inicio: string, fin: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/reportes/ingresos?inicio=${inicio}&fin=${fin}`);
  }

  // --- MÉTODOS DE HABILIDADES ---
  obtenerHabilidadesAdmin(): Observable<any> {
    return this.http.get(`${this.apiUrl}/habilidades`);
  }

  crearHabilidad(idCategoria: number, nombre: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/crear-habilidad`, { idCategoria, nombre });
  }

  editarHabilidad(idHabilidad: number, idCategoria: number, nombre: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/editar-habilidad`, { idHabilidad, idCategoria, nombre });
  }

  cambiarEstadoHabilidad(idHabilidad: number, estado: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/estado-habilidad`, { idHabilidad, estado });
  }
}