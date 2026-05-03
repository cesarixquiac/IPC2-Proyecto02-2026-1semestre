import { Component, OnInit } from '@angular/core'; 
import { CommonModule } from '@angular/common'; 
import { Router, RouterModule } from '@angular/router';
import { Auth } from '../../../services/auth';
import { ClienteService } from '../../../services/cliente'; 
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard-cliente.html',
  styleUrl: './dashboard-cliente.css'
})
export class DashboardClienteComponent implements OnInit {

  cargando: boolean = true; // Empieza en true para ocultar el dashboard al inicio
  misProyectos: any[] = [];
  mensajeError: string = '';

  constructor(
    private authService: Auth,
    private clienteService: ClienteService,
    private proyectoService: ProyectoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.revisarPerfil();
  }

  revisarPerfil() {
    this.clienteService.verificarPerfil().subscribe({
      next: (res) => {
        // Si responde 200 OK (existe: true), lo dejamos ver el Dashboard
        this.cargando = false;
        this.cargarMisProyectos();
      },
      error: (err) => {
        // Si el backend responde 404 (Not Found), lo enviamos a completar sus datos
        if (err.status === 404) {
          this.router.navigate(['/cliente/completar-perfil']);
        } else {
          // Si es otro error (ej. se cayó el servidor), lo mostramos en consola
          console.error("Error al verificar perfil", err);
          this.cargando = false; 
        }
      }
    });
  }

  cargarMisProyectos() {
    this.proyectoService.obtenerMisProyectos().subscribe({
      next: (data) => {
        this.misProyectos = data;
      },
      error: (err) => {
        this.mensajeError = "No se pudieron cargar tus proyectos.";
        console.error(err);
      }
    });
  }



  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }
}