import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from '../../../services/auth';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule], // Importamos FormsModule para usar ngModel
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  credenciales = {
    username: '',
    password: ''
  };
  mensajeError: string = '';

  constructor(private authService: Auth, private router: Router) {}

  iniciarSesion() {
    this.mensajeError = '';
    
    if (!this.credenciales.username || !this.credenciales.password) {
      this.mensajeError = 'Por favor, completa todos los campos.';
      return;
    }

    this.authService.login(this.credenciales).subscribe({
      next: () => {
        const usuario = this.authService.obtenerDatosUsuario();
        
        // Redirigir según el rol
        if (usuario.rol === 'CLIENTE') {
          this.router.navigate(['/cliente/dashboard']);
        } else if (usuario.rol === 'FREELANCER') {
          this.router.navigate(['/freelancer/dashboard']);
        } else if (usuario.rol === 'ADMIN') {
          this.router.navigate(['/admin/dashboard']); // Si tienes este rol
        }
      },
      error: (err) => {
        this.mensajeError = err.error?.error || 'Credenciales incorrectas o error en el servidor.';
      }
    });
  }
}