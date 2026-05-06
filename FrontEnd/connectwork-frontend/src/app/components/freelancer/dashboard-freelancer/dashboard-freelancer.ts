import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // <-- IMPORTANTE PARA EL MODAL
import { Auth } from '../../../services/auth';
import { ProyectoService } from '../../../services/proyecto';
import { FreelancerService } from '../../../services/freelancer'; 

@Component({
  selector: 'app-dashboard-freelancer',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // <-- AGREGA FormsModule AQUÍ
  templateUrl: './dashboard-freelancer.html',
  styleUrl: './dashboard-freelancer.css'
})
export class DashboardFreelancerComponent implements OnInit {
  proyectosDisponibles: any[] = [];
  proyectosFiltrados: any[] = []; // <-- Este es el que mostraremos en pantalla
  mensajeError: string = '';

  // Variables para los filtros
  filtroCategoria: string = '';
  filtroHabilidad: string = '';
  filtroPresupuestoMin: number | null = null;
  filtroPresupuestoMax: number | null = null;


 
  
  // Tu brillante idea del Saldo
  saldoAcumulado: number = 0.00; // Luego lo traeremos del backend

  // Variables para el modal
  proyectoSeleccionado: any = null;
  nuevaPropuesta = {
    idProyecto: 0,
    montoOfertado: null,
    plazoDias: null,
    cartaPresentacion: ''
  };

  constructor(
    private authService: Auth,
    private proyectoService: ProyectoService,
    private freelancerService: FreelancerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.freelancerService.verificarPerfilEstado().subscribe({
      next: (res) => {
        this.cargarProyectos();
        
        // ¡NUEVO! Traemos el dinero real de la base de datos
        this.freelancerService.obtenerSaldo().subscribe(
          data => this.saldoAcumulado = data.saldo
        );
      },
      error: (err) => {
        if (err.status === 404) {
          this.router.navigate(['/freelancer/completar-perfil']);
        }
      }
    });
  }

  cargarProyectos() {
    this.proyectoService.obtenerProyectosDisponibles().subscribe({
      next: (data) => {
        this.proyectosDisponibles = data;
        this.proyectosFiltrados = data; 
      },
      error: (err) => this.mensajeError = 'Error: ' + (err.error?.error || '')
    });
  }

  // LÓGICA MÁGICA DE FILTRADO EN TIEMPO REAL
  aplicarFiltros() {
    this.proyectosFiltrados = this.proyectosDisponibles.filter(p => {
      let cumpleCategoria = true;
      let cumpleHabilidad = true;
      let cumplePresupuestoMin = true;
      let cumplePresupuestoMax = true;

      if (this.filtroCategoria) {
        cumpleCategoria = p.categoria.toLowerCase().includes(this.filtroCategoria.toLowerCase());
      }
      if (this.filtroHabilidad) {
        // Asumiendo que p.habilidades es un string separado por comas
        cumpleHabilidad = p.habilidades.toLowerCase().includes(this.filtroHabilidad.toLowerCase());
      }
      if (this.filtroPresupuestoMin !== null && this.filtroPresupuestoMin > 0) {
        cumplePresupuestoMin = p.presupuestoMaximo >= this.filtroPresupuestoMin;
      }
      if (this.filtroPresupuestoMax !== null && this.filtroPresupuestoMax > 0) {
        cumplePresupuestoMax = p.presupuestoMaximo <= this.filtroPresupuestoMax;
      }

      return cumpleCategoria && cumpleHabilidad && cumplePresupuestoMin && cumplePresupuestoMax;
    });
  }

  limpiarFiltros() {
    this.filtroCategoria = '';
    this.filtroHabilidad = '';
    this.filtroPresupuestoMin = null;
    this.filtroPresupuestoMax = null;
    this.proyectosFiltrados = [...this.proyectosDisponibles]; // Restauramos todo
  }



  abrirModalPropuesta(proyecto: any) {
    this.proyectoSeleccionado = proyecto;
    this.nuevaPropuesta = {
      idProyecto: proyecto.id,
      montoOfertado: null,
      plazoDias: null,
      cartaPresentacion: ''
    };
  }

  enviarPropuestaFinal() {
    if (!this.nuevaPropuesta.montoOfertado || !this.nuevaPropuesta.plazoDias || !this.nuevaPropuesta.cartaPresentacion) {
      alert("Por favor, llena todos los campos de la propuesta.");
      return;
    }
    if (this.nuevaPropuesta.montoOfertado > this.proyectoSeleccionado.presupuestoMaximo) {
      alert("Tu oferta no puede superar el presupuesto máximo del cliente.");
      return;
    }

    this.proyectoService.enviarPropuesta(this.nuevaPropuesta).subscribe({
      next: (res) => {
        alert("¡Propuesta enviada con éxito!");
        this.cargarProyectos(); // Recargamos para que el botón se bloquee
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }

  // Variables para el formulario del modal
  nuevaHabilidad = {
    nombre: '',
    descripcion: ''
  };

  // Función para enviar la solicitud
  enviarSolicitudHabilidad() {
    if (!this.nuevaHabilidad.nombre.trim()) {
      alert(" El nombre de la habilidad es obligatorio.");
      return;
    }

    this.freelancerService.solicitarNuevaHabilidad(this.nuevaHabilidad.nombre, this.nuevaHabilidad.descripcion).subscribe({
      next: (res) => {
        alert(" " + res.mensaje);
        // Limpiamos el formulario
        this.nuevaHabilidad = { nombre: '', descripcion: '' };
        
        // Cerramos el modal usando vanilla JS (opcional, para que se cierre solo)
        const modalElement = document.getElementById('modalSolicitarHabilidad');
        if (modalElement) {
          const bootstrapModal = (window as any).bootstrap.Modal.getInstance(modalElement);
          if (bootstrapModal) bootstrapModal.hide();
        }
      },
      error: (err) => {
        console.error("Error al solicitar habilidad:", err);
        alert("Ocurrió un error al enviar la solicitud.");
      }
    });
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }

  
}