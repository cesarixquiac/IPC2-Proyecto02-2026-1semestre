import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Auth } from '../../../services/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './registro.html',
  styleUrl: './registro.css'
})
export class RegistroComponent {
  nuevoUsuario = {
    nombreCompleto: '',
    username: '',
    password: '',
    email: '',
    telefono: '',
    direccion: '',
    cui: '',
    fechaNacimiento: '',
    rol: '' 
  };

  mensajeError: string = '';
  mensajeExito: string = '';

  constructor(private authService: Auth, private router: Router) {}

  registrar() {
    this.mensajeError = '';
    this.mensajeExito = '';

    // Validamos que al menos los campos esenciales no estén vacíos
    if (!this.nuevoUsuario.nombreCompleto || !this.nuevoUsuario.username || 
        !this.nuevoUsuario.password || !this.nuevoUsuario.email || 
        !this.nuevoUsuario.cui || !this.nuevoUsuario.rol) {
      this.mensajeError = 'Por favor, completa todos los campos obligatorios.';
      return;
    }

    this.authService.registrar(this.nuevoUsuario).subscribe({
      next: (res) => {
        this.mensajeExito = '¡Cuenta creada con éxito! Redirigiendo al login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        // Tu backend devolverá el mensaje de error si el username o correo ya existen
        this.mensajeError = err.error?.error || 'Ocurrió un error al registrar el usuario.';
      }
    });
  }
}