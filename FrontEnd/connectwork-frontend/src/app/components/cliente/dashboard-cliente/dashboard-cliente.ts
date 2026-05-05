import { Component, OnInit } from '@angular/core'; 
import { CommonModule } from '@angular/common'; 
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // <-- IMPORTANTE PARA EL MODAL
import { Auth } from '../../../services/auth';
import { ClienteService } from '../../../services/cliente'; 
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule], // <-- AGREGADO AQUÍ
  templateUrl: './dashboard-cliente.html',
  styleUrl: './dashboard-cliente.css'
})
export class DashboardClienteComponent implements OnInit {

  cargando: boolean = true; 
  misProyectos: any[] = [];
  mensajeError: string = '';
  
  // Nuevas variables para el perfil y el modal
  cliente: any = { saldoDisponible: 0 }; 
  montoRecarga: number = 0;

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
        this.cargando = false;
        this.cargarMisProyectos();
        this.cargarDatosPerfil(); // <-- Cargamos el saldo una vez que sabemos que existe
      },
      error: (err) => {
        if (err.status === 404) {
          this.router.navigate(['/cliente/completar-perfil']);
        } else {
          console.error("Error al verificar perfil", err);
          this.cargando = false; 
        }
      }
    });
  }

  // Nuevo método para obtener el saldo actual
  cargarDatosPerfil() {
    this.clienteService.obtenerPerfil().subscribe({
      next: (data) => {
        this.cliente = data;
      },
      error: (err) => console.error("Error al cargar los datos del perfil", err)
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

  procesarRecarga() {
    if (this.montoRecarga <= 0) return;

    this.clienteService.recargarSaldo(this.montoRecarga).subscribe({
      next: (res) => {
        alert(res.mensaje);
        this.montoRecarga = 0;
        this.cargarDatosPerfil(); // <-- Actualizamos el saldo mágicamente sin recargar la página
      },
      error: (err) => alert("Error al recargar: " + (err.error?.error || "Desconocido"))
    });
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/login']);
  }


  proyectoAEditar: any = { id: 0, titulo: '', descripcion: '', presupuestoMaximo: 0, fechaLimite: '' };

  abrirModalEditar(proyecto: any) {
  
    this.proyectoAEditar = { ...proyecto };
  }

  guardarEdicion() {
    this.proyectoService.editarProyecto(this.proyectoAEditar.id, this.proyectoAEditar).subscribe({
      next: (res) => {
        alert("¡Proyecto actualizado!");
        this.cargarMisProyectos(); // Recargamos la tabla
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }

  eliminarProyecto(idProyecto: number) {
    if (confirm("¿Estás seguro de que deseas eliminar este proyecto de forma permanente?")) {
      this.proyectoService.eliminarProyecto(idProyecto).subscribe({
        next: (res) => {
          alert("Proyecto eliminado.");
          this.cargarMisProyectos(); // Recargamos la tabla
        },
        error: (err) => alert("Error al eliminar: " + err.error?.error)
      });
    }
  }


}