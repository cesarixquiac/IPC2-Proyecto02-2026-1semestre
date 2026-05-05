import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-ver-entregas',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './ver-entregas.html'
})
export class VerEntregasComponent implements OnInit {
  idProyecto!: number;
  proyecto: any = null;
  entrega: any = null; // Aquí guardaremos los detalles de la entrega (link, descripción)
  
  // Variables para los modales
  motivoRechazo: string = '';
  motivoCancelacion: string = '';

  constructor(
    private route: ActivatedRoute,
    private proyectoService: ProyectoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.idProyecto = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarDatosProyecto();
  }

  cargarDatosProyecto() {
    // Necesitamos un método en el servicio que traiga el estado del proyecto y su entrega
    this.proyectoService.obtenerDetalleEntrega(this.idProyecto).subscribe({
      next: (data) => {
        this.proyecto = data.proyecto;
        this.entrega = data.entrega;
      },
      error: (err) => console.error("Error al cargar la entrega", err)
    });
  }

  aprobarEntrega() {
    if (confirm("¿Estás seguro de aprobar esta entrega? El pago será liberado al freelancer.")) {
      this.proyectoService.aprobarEntrega(this.idProyecto).subscribe({
        next: (res) => {
          alert("¡Entrega aprobada! El contrato ha finalizado exitosamente.");
          // Aquí podríamos redirigir a la pantalla de calificar freelancer
          this.router.navigate(['/cliente/dashboard']);
        },
        error: (err) => alert("Error: " + err.error?.error)
      });
    }
  }

  procesarRechazo() {
    if (this.motivoRechazo.trim().length < 10) {
      alert("Por favor, ingresa un motivo detallado (mínimo 10 caracteres) para que el freelancer pueda corregirlo.");
      return;
    }

    this.proyectoService.rechazarEntrega(this.idProyecto, this.motivoRechazo).subscribe({
      next: (res) => {
        alert("Entrega rechazada. El proyecto vuelve a estado EN_PROGRESO.");
        this.motivoRechazo = '';
        this.cargarDatosProyecto(); // Recargamos para ver el cambio de estado
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }

  procesarCancelacion() {
    if (this.motivoCancelacion.trim().length < 10) {
      alert("Debes justificar el motivo de la cancelación.");
      return;
    }

    this.proyectoService.cancelarContrato(this.idProyecto, this.motivoCancelacion).subscribe({
      next: (res) => {
        alert("Contrato cancelado. Los fondos han sido devueltos a tu cuenta.");
        this.router.navigate(['/cliente/dashboard']);
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }

  calificacion = { estrellas: 5, comentario: '' };

  confirmarAprobacion() {
    this.proyectoService.aprobarEntregaConCalificacion(this.idProyecto, this.calificacion).subscribe({
      next: (res) => {
        alert("¡Entrega aprobada! El proyecto se ha completado y el freelancer ha sido calificado.");
        this.router.navigate(['/cliente/dashboard']);
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }
}