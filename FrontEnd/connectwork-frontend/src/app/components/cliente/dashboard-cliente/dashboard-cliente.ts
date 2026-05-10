import { Component, OnInit } from '@angular/core'; 
import { CommonModule } from '@angular/common'; 
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { Auth } from '../../../services/auth';
import { ClienteService } from '../../../services/cliente'; 
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './dashboard-cliente.html',
  styleUrl: './dashboard-cliente.css'
})
export class DashboardClienteComponent implements OnInit {

  cargando: boolean = true; 
  misProyectos: any[] = [];
  mensajeError: string = '';
  
  cliente: any = { saldoDisponible: 0 }; 
  montoRecarga: number = 0;

  // --- VARIABLES DE REPORTES ---
  fechaInicio: string = '';
  fechaFin: string = '';
  historialProyectos: any[] = [];
  historialRecargas: any[] = [];
  gastosCategoria: any[] = [];
  
  // ¡Variable iniciada en 0! Se llenará mágicamente en el ngOnInit
  idClienteActual: number = 0; 

  constructor(
    private authService: Auth,
    private clienteService: ClienteService,
    private proyectoService: ProyectoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.revisarPerfil();

    // Sacamos tu ID real del Token
    const datosUsuario = this.authService.obtenerDatosUsuario();
    if (datosUsuario && datosUsuario.idUsuario) {
      this.idClienteActual = datosUsuario.idUsuario; 
    }

    
    if (this.idClienteActual > 0) {
      this.cargarHistorialRecargas();
    }
  }

  revisarPerfil() {
    this.clienteService.verificarPerfil().subscribe({
      next: (res) => {
        this.cargando = false;
        this.cargarMisProyectos();
        this.cargarDatosPerfil();
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
        this.cargarDatosPerfil();
        this.cargarHistorialRecargas(); // <-- Actualiza la tabla de recargas al instante
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
        this.cargarMisProyectos();
      },
      error: (err) => alert("Error: " + err.error?.error)
    });
  }

  eliminarProyecto(idProyecto: number) {
    if (confirm("¿Estás seguro de que deseas eliminar este proyecto de forma permanente?")) {
      this.proyectoService.eliminarProyecto(idProyecto).subscribe({
        next: (res) => {
          alert("Proyecto eliminado.");
          this.cargarMisProyectos();
        },
        error: (err) => alert("Error al eliminar: " + err.error?.error)
      });
    }
  }

  // --- MÉTODOS DE REPORTES ---
  cargarHistorialRecargas() {
    if (this.idClienteActual > 0) {
      this.clienteService.reporteRecargas(this.idClienteActual).subscribe(data => this.historialRecargas = data);
    }
  }

  generarReportes() {
    if (!this.fechaInicio || !this.fechaFin) {
      alert("⚠️ Selecciona ambas fechas.");
      return;
    }

    this.clienteService.reporteProyectos(this.idClienteActual, this.fechaInicio, this.fechaFin).subscribe(data => this.historialProyectos = data);
    this.clienteService.reporteGastos(this.idClienteActual, this.fechaInicio, this.fechaFin).subscribe(data => this.gastosCategoria = data);
    
    this.cargarHistorialRecargas(); 
  }

  imprimirReportes() {
    window.print();
  }

  solicitudCat = { nombre: '', descripcion: '' };

  enviarSolicitudCategoria() {
    if (!this.solicitudCat.nombre.trim() || !this.solicitudCat.descripcion.trim()) {
      alert("⚠️ Por favor ingresa el nombre y una breve descripción.");
      return;
    }
    
    this.clienteService.solicitarCategoria(this.idClienteActual, this.solicitudCat.nombre, this.solicitudCat.descripcion).subscribe({
      next: (res) => {
        alert("✅ " + res.mensaje);
        this.solicitudCat = { nombre: '', descripcion: '' }; // Limpiar formulario
      },
      error: (err) => alert("❌ Error: " + (err.error?.error || "Desconocido"))
    });
  }

}