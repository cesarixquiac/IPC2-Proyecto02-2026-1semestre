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
  mensajeError: string = '';
  
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
      next: (data) => this.proyectosDisponibles = data,
      error: (err) => this.mensajeError = 'Error: ' + (err.error?.error || '')
    });
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

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }

  
}