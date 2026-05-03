import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProyectoService } from '../../../services/proyecto';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../../services/cliente';
import { FormsModule } from '@angular/forms';
declare var bootstrap: any;

@Component({
  selector: 'app-ver-propuestas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ver-propuestas.html'
})
export class VerPropuestasComponent implements OnInit {
  idProyecto!: number;
  propuestas: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private proyectoService: ProyectoService,
    private router: Router,
    private clienteService: ClienteService
  ) { }

  ngOnInit() {
    // Obtenemos el ID del proyecto desde la URL
    this.idProyecto = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarPropuestas();
  }

  cargarPropuestas() {
    this.proyectoService.obtenerPropuestasProyecto(this.idProyecto).subscribe({
      next: (data) => this.propuestas = data,
      error: (err) => console.error(err)
    });
  }

  aceptar(idPropuesta: number) {
    this.proyectoService.aceptarPropuesta(this.idProyecto, idPropuesta).subscribe({
      next: (res) => {
        alert("¡Proyecto iniciado!");
        this.router.navigate(['/cliente/dashboard']);
      },
      error: (err) => {
        const mensajeError = err?.error?.error || "Ocurrió un error inesperado al aceptar.";
        if (mensajeError.includes("Saldo insuficiente") || mensajeError.includes("saldo")) {
          if (confirm("No tienes saldo suficiente o debes recargar. ¿Deseas recargar ahora?")) {
            const modalElement = document.getElementById('modalRecarga');
            if (modalElement && typeof bootstrap !== 'undefined') {
              const modal = new bootstrap.Modal(modalElement);
              modal.show();
            } else {
              alert("Por favor dirígete a la sección de recarga.");
            }
          }
        } else {
          alert("Error: " + (err.error?.error || err.message));
        }
      }
    });
  }

  rechazar(idPropuesta: number) {
    if (confirm("¿Seguro que deseas rechazar esta propuesta?")) {
      this.proyectoService.rechazarPropuesta(idPropuesta).subscribe({
        next: (res) => {
          // Recargamos la lista para que desaparezca o actualice su estado
          this.cargarPropuestas();
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }

  // Agrega estas variables arriba
  montoRecarga: number = 0;


  procesarRecarga() {
    const modalElement = document.getElementById('modalRecarga');
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();

    if (this.montoRecarga <= 0) {
      alert("Por favor ingresa un monto válido.");
      return;
    }

    this.clienteService.recargarSaldo(this.montoRecarga).subscribe({
      next: (res) => {
        alert(res.mensaje);
        this.montoRecarga = 0;

        // Cerramos el modal usando JavaScript nativo de Bootstrap
        const modalElement = document.getElementById('modalRecarga');
        const modal = (window as any).bootstrap.Modal.getInstance(modalElement);
        modal.hide();
      },
      error: (err) => alert("Error al recargar: " + err.error.error)
    });
  }


}