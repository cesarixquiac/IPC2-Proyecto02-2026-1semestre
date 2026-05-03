import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Auth } from '../../../services/auth';
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-dashboard-freelancer',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-freelancer.html',
  styleUrl: './dashboard-freelancer.css'
})
export class DashboardFreelancerComponent implements OnInit {
  proyectosDisponibles: any[] = [];
  mensajeError: string = '';

  constructor(
    private authService: Auth,
    private proyectoService: ProyectoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarProyectos();
  }

  cargarProyectos() {
    this.proyectoService.obtenerProyectosDisponibles().subscribe({
      next: (data) => {
        this.proyectosDisponibles = data;
      },
      error: (err) => {
        this.mensajeError = 'No se pudieron cargar los proyectos. ' + (err.error?.error || '');
      }
    });
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}