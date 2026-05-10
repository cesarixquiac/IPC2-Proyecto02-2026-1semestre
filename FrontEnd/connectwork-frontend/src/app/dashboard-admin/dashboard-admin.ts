import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AdminService } from '../services/admin';
import { Auth } from '../services/auth'; // Tu servicio de autenticación

@Component({
  selector: 'app-dashboard-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dashboard-admin.html'
})
export class DashboardAdminComponent implements OnInit {

// Control de Vistas (Pestañas)
  vistaActual: string = 'inicio'; // Puede ser 'inicio', 'catalogo' o 'reportes'

  cambiarVista(vista: string) {
    this.vistaActual = vista;
  }

  solicitudes: any[] = [];
  categorias: any[] = [];
  
  comisionActual: number = 0; 
  nuevaComision: number | null = null;
  // Variables para el modal
  solicitudSeleccionada: any = null;
  idCategoriaDestino: number | null = null;
  
  categoriasAdmin: any[] = [];
  isEditandoCategoria: boolean = false;

  categoriaForm = {
    idCategoria: 0,
    nombre: '',
    descripcion: ''
  };

  fechaInicio: string = '';
  fechaFin: string = '';
  
  datosHistorialComisiones: any[] = [];
  datosTopFreelancers: any[] = [];
  datosTopCategorias: any[] = [];
  datosIngresos: any = null;
  

  constructor(
    private adminService: AdminService,
    private authService: Auth,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
    this.cargarCategorias();
    this.cargarComisionActual();
    this.cargarCategoriasAdmin()
    this.cargarHabilidadesAdmin();
  }

  cargarSolicitudes() {
    this.adminService.obtenerSolicitudes().subscribe({
      next: (data: any) => this.solicitudes = data || [],
      error: (err: any) => console.error("Error al cargar solicitudes", err)
    });
  }

  cargarCategorias() {
    this.adminService.obtenerCategorias().subscribe({
      next: (data) => {
        this.categorias = data || [];
        console.log("CATEGORÍAS CARGADAS:", this.categorias); // <-- AGREGA ESTO
      },
      error: (err) => console.error("Error al cargar categorías", err)
    });
  }

  rechazarSolicitud(idSolicitud: number) {
    if (confirm("¿Estás seguro de rechazar esta solicitud?")) {
      this.adminService.procesarSolicitud(idSolicitud, 'RECHAZADA').subscribe({
        next: (res: any) => {
          alert(" Solicitud rechazada.");
          this.cargarSolicitudes(); // Recargar la tabla
        },
        error: (err: any) => alert("Error: " + err.error?.error)
      });
    }
  }

  abrirModalAceptar(solicitud: any) {
    if (solicitud.tipo_solicitud === 'CATEGORIA') {
      // Las categorías no necesitan modal, se aceptan directo
      if (confirm(`¿Aceptar la nueva categoría: ${solicitud.nombre_sugerido}?`)) {
        this.procesarAceptacion(solicitud.id_solicitud, 'ACEPTADA');
      }
    } else {
      
      this.solicitudSeleccionada = solicitud;
      this.idCategoriaDestino = null;
      
      
      const modalElement = document.getElementById('modalAceptarHabilidad');
      if (modalElement) {
        const bootstrapModal = new (window as any).bootstrap.Modal(modalElement);
        bootstrapModal.show();
      }
    }
  }

  confirmarAceptacionHabilidad() {
    if (!this.idCategoriaDestino) {
      alert("Debes seleccionar una categoría para esta habilidad.");
      return;
    }
    this.procesarAceptacion(this.solicitudSeleccionada.id_solicitud, 'ACEPTADA', this.idCategoriaDestino);
  }

  private procesarAceptacion(idSolicitud: number, accion: string, idCategoria?: number) {
    this.adminService.procesarSolicitud(idSolicitud, accion, idCategoria).subscribe({
      next: (res: any) => {
        alert(" " + (res?.mensaje || 'Solicitud aprobada correctamente.'));
        this.cargarSolicitudes(); // Recargar la tabla
        
        // Cerrar modal si estaba abierto
        const modalElement = document.getElementById('modalAceptarHabilidad');
        if (modalElement) {
          const bootstrapModal = (window as any).bootstrap.Modal.getInstance(modalElement);
          if (bootstrapModal) bootstrapModal.hide();
        }
      },
      error: (err: any) => alert("Error: " + err.error?.error)
    });
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }

  // --- MÉTODOS DE COMISIÓN ---
  cargarComisionActual() {
    this.adminService.obtenerComisionActual().subscribe({
      next: (data) => this.comisionActual = data.porcentaje || 0,
      error: (err) => console.error("Error al cargar comisión", err)
    });
  }

  guardarNuevaComision() {
    if (!this.nuevaComision || this.nuevaComision <= 0 || this.nuevaComision >= 100) {
      alert(" Por favor, ingresa un porcentaje válido (mayor a 0 y menor a 100).");
      return;
    }

    if (confirm(`¿Estás seguro de cambiar la comisión global al ${this.nuevaComision}%?\n\nEste cambio aplicará únicamente a los NUEVOS contratos.`)) {
      this.adminService.actualizarComision(this.nuevaComision).subscribe({
        next: (res) => {
          alert(" " + res.mensaje);
          this.cargarComisionActual(); // Recargamos para ver el nuevo valor
          this.nuevaComision = null;   // Limpiamos el input
        },
        error: (err) => alert(" Error: " + (err.error?.error || 'No se pudo actualizar'))
      });
    }
  }

  // --- MÉTODOS DE GESTIÓN DE CATEGORÍAS ---
  
  cargarCategoriasAdmin() {
    this.adminService.obtenerCategoriasAdmin().subscribe({
      next: (data) => this.categoriasAdmin = data || [],
      error: (err) => console.error("Error al cargar categorías admin", err)
    });
  }

  abrirModalCategoria(categoria?: any) {
    if (categoria) {
      // Modo Editar
      this.isEditandoCategoria = true;
      this.categoriaForm = {
        idCategoria: categoria.id_categoria,
        nombre: categoria.nombre,
        descripcion: categoria.descripcion
      };
    } else {
      // Modo Crear Nuevo
      this.isEditandoCategoria = false;
      this.categoriaForm = { idCategoria: 0, nombre: '', descripcion: '' };
    }
  }

  guardarCategoria() {
    if (!this.categoriaForm.nombre.trim()) {
      alert(" El nombre de la categoría es obligatorio.");
      return;
    }

    if (this.isEditandoCategoria) {
      this.adminService.editarCategoria(this.categoriaForm.idCategoria, this.categoriaForm.nombre, this.categoriaForm.descripcion).subscribe({
        next: (res) => this.finalizarGuardadoCategoria(res.mensaje),
        error: (err) => alert(" Error: " + err.error?.error)
      });
    } else {
      this.adminService.crearCategoria(this.categoriaForm.nombre, this.categoriaForm.descripcion).subscribe({
        next: (res) => this.finalizarGuardadoCategoria(res.mensaje),
        error: (err) => alert(" Error: " + err.error?.error)
      });
    }
  }

  private finalizarGuardadoCategoria(mensaje: string) {
    alert(" " + mensaje);
    this.cargarCategoriasAdmin(); // Recargar la tabla
    this.cargarCategorias(); // Recargar la lista del modal de habilidades por si acaso
    
    // Cerrar modal
    const modalElement = document.getElementById('modalCategoria');
    if (modalElement) {
      const bootstrapModal = (window as any).bootstrap.Modal.getInstance(modalElement) || new (window as any).bootstrap.Modal(modalElement);
      bootstrapModal.hide();
    }
  }

  toggleEstadoCategoria(categoria: any) {
    const nuevoEstado = categoria.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    const accion = nuevoEstado === 'ACTIVO' ? 'activar' : 'desactivar';
    
    if (confirm(`¿Estás seguro de ${accion} la categoría "${categoria.nombre}"?`)) {
      this.adminService.cambiarEstadoCategoria(categoria.id_categoria, nuevoEstado).subscribe({
        next: (res) => {
          alert(" Estado actualizado");
          this.cargarCategoriasAdmin();
        },
        error: (err) => alert(" Error: " + err.error?.error)
      });
    }
  }

  // Cargar historial de comisiones (este no necesita fechas)
  cargarHistorialComisiones() {
    this.adminService.reporteComisiones().subscribe(data => this.datosHistorialComisiones = data);
  }

  // Generar los reportes que dependen de fechas
  generarReportes() {
    if (!this.fechaInicio || !this.fechaFin) {
      alert(" Por favor selecciona ambas fechas.");
      return;
    }

    this.adminService.reporteTopFreelancers(this.fechaInicio, this.fechaFin).subscribe(data => this.datosTopFreelancers = data);
    this.adminService.reporteTopCategorias(this.fechaInicio, this.fechaFin).subscribe(data => this.datosTopCategorias = data);
    this.adminService.reporteIngresos(this.fechaInicio, this.fechaFin).subscribe(data => this.datosIngresos = data);
    
    // También cargamos el historial por si hubo cambios
    this.cargarHistorialComisiones();
  }

  imprimirReporte() {
    window.print();
  }

  // Variables para Gestión de Habilidades
  habilidadesAdmin: any[] = [];
  isEditandoHabilidad: boolean = false;
  habilidadForm = {
    idHabilidad: 0,
    idCategoria: null as number | null,
    nombre: ''
  }

  // --- MÉTODOS DE HABILIDADES ---
  cargarHabilidadesAdmin() {
    this.adminService.obtenerHabilidadesAdmin().subscribe({
      next: (data) => this.habilidadesAdmin = data || [],
      error: (err) => console.error("Error al cargar habilidades", err)
    });
  }

  abrirModalHabilidad(habilidad?: any) {
    if (habilidad) {
      this.isEditandoHabilidad = true;
      this.habilidadForm = {
        idHabilidad: habilidad.id_habilidad,
        idCategoria: habilidad.id_categoria,
        nombre: habilidad.nombre
      };
    } else {
      this.isEditandoHabilidad = false;
      this.habilidadForm = { idHabilidad: 0, idCategoria: null, nombre: '' };
    }
  }

  guardarHabilidad() {
    if (!this.habilidadForm.nombre.trim() || !this.habilidadForm.idCategoria) {
      alert(" El nombre y la categoría son obligatorios.");
      return;
    }

    if (this.isEditandoHabilidad) {
      this.adminService.editarHabilidad(this.habilidadForm.idHabilidad, this.habilidadForm.idCategoria, this.habilidadForm.nombre).subscribe({
        next: (res) => this.finalizarGuardadoHabilidad(res.mensaje),
        error: (err) => alert(" Error: " + err.error?.error)
      });
    } else {
      this.adminService.crearHabilidad(this.habilidadForm.idCategoria, this.habilidadForm.nombre).subscribe({
        next: (res) => this.finalizarGuardadoHabilidad(res.mensaje),
        error: (err) => alert(" Error: " + err.error?.error)
      });
    }
  }

  private finalizarGuardadoHabilidad(mensaje: string) {
    alert(" " + mensaje);
    this.cargarHabilidadesAdmin();
    const modalElement = document.getElementById('modalHabilidad');
    if (modalElement) {
      const bootstrapModal = (window as any).bootstrap.Modal.getInstance(modalElement) || new (window as any).bootstrap.Modal(modalElement);
      bootstrapModal.hide();
    }
  }

  toggleEstadoHabilidad(hab: any) {
    const nuevoEstado = hab.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    if (confirm(`¿Cambiar estado de "${hab.nombre}" a ${nuevoEstado}?`)) {
      this.adminService.cambiarEstadoHabilidad(hab.id_habilidad, nuevoEstado).subscribe({
        next: () => this.cargarHabilidadesAdmin(),
        error: (err) => alert(" Error: " + err.error?.error)
      });
    }
  }


  
}