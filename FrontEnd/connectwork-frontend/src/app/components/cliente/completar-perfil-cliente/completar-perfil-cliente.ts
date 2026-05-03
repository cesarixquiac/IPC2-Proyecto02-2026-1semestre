import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClienteService } from '../../../services/cliente';

@Component({
  selector: 'app-completar-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './completar-perfil-cliente.html',   
  styleUrl: './completar-perfil-cliente.css'
})
export class CompletarPerfilComponent {
 
  perfil = {
    descripcionEmpresa: '',
    sectorIndustria: '',
    sitioWeb: ''
  };

  mensajeError: string = '';

  constructor(private clienteService: ClienteService, private router: Router) {}

  guardarPerfil() {
    this.mensajeError = '';

    if (!this.perfil.descripcionEmpresa || !this.perfil.sectorIndustria) {
      this.mensajeError = 'La descripción y el sector son obligatorios.';
      return;
    }

    this.clienteService.completarPerfil(this.perfil).subscribe({
      next: (res) => {
     
        this.router.navigate(['/cliente/dashboard']);
      },
      error: (err) => {
        this.mensajeError = err.error?.error || 'Error al guardar el perfil.';
      }
    });
  }
}