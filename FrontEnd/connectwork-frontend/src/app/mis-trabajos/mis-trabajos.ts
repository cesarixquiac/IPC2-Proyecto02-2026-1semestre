import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ProyectoService } from '../services/proyecto';

@Component({
  selector: 'app-mis-trabajos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './mis-trabajos.html',
  styleUrl: './mis-trabajos.css'
})
export class MisTrabajosComponent implements OnInit {

  contratosActivos: any[] = [];
  mensajeError: string = '';

  // Variables para el modal de entrega
  contratoSeleccionado: any = null;
  nuevaEntrega = {
    idContrato: 0,
    urlArchivo: '',
    descripcion: ''
  };

  constructor(private proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.cargarContratos();
  }

  cargarContratos() {
    this.proyectoService.obtenerMisContratos().subscribe({
      next: (data) => this.contratosActivos = data,
      error: (err) => this.mensajeError = 'Error al cargar tus trabajos: ' + (err.error?.error || '')
    });
  }

  abrirModalEntrega(contrato: any) {
    this.contratoSeleccionado = contrato;
    this.nuevaEntrega = {
      // ¡CORREGIDO! Ahora toma el idContrato real que viene de la BD
      idContrato: contrato.idContrato, 
      urlArchivo: '',
      descripcion: ''
    };
  }

  enviarTrabajo() {
    if (!this.nuevaEntrega.urlArchivo || !this.nuevaEntrega.descripcion) {
      alert("Debes proporcionar la URL del archivo y una descripción del trabajo.");
      return;
    }

    this.proyectoService.subirEntrega(this.nuevaEntrega).subscribe({
      next: (res) => {
        alert("¡Trabajo entregado con éxito! Esperando revisión del cliente.");
        this.cargarContratos(); // Recargamos para que el estado cambie a ENTREGA_PENDIENTE
      },
      error: (err) => alert("Error al subir entrega: " + err.error?.error)
    });
  }
}