import { Component, OnInit } from '@angular/core'; // <-- Importamos OnInit
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ProyectoService } from '../../../services/proyecto';

@Component({
  selector: 'app-publicar-proyecto',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './publicar-proyecto.html',
  styleUrl: './publicar-proyecto.css'
})
export class PublicarProyectoComponent implements OnInit { // <-- Implementamos la interfaz
  nuevoProyecto = {
    titulo: '',
    descripcion: '',
    idCategoria: null, 
    presupuestoMaximo: null,
    fechaLimite: '',
    habilidadesRequeridas: [] 
  };
  
  // Dejamos las listas vacías, se llenarán solas
  categorias: any[] = [];
  habilidades: any[] = [];

  mensajeExito: string = '';
  mensajeError: string = '';

  constructor(private proyectoService: ProyectoService, private router: Router) {}

  // Este método se ejecuta automáticamente al abrir la pantalla
  ngOnInit(): void {
    this.cargarCatalogos();
  }

  cargarCatalogos() {
    // Pedimos las categorías al backend
    this.proyectoService.obtenerCategorias().subscribe({
      next: (data) => this.categorias = data,
      error: (err) => console.error('Error cargando categorías', err)
    });

    // Pedimos las habilidades al backend
    this.proyectoService.obtenerHabilidades().subscribe({
      next: (data) => {
        // Opcional: Filtrar solo las que están en estado 'ACTIVO'
        this.habilidades = data.filter((h: any) => h.estado === 'ACTIVO');
      },
      error: (err) => console.error('Error cargando habilidades', err)
    });
  }

  publicar() {
    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.nuevoProyecto.titulo || !this.nuevoProyecto.descripcion || 
        !this.nuevoProyecto.presupuestoMaximo || !this.nuevoProyecto.fechaLimite || 
        !this.nuevoProyecto.idCategoria || this.nuevoProyecto.habilidadesRequeridas.length === 0) {
      this.mensajeError = 'Por favor, llena todos los campos obligatorios.';
      return;
    }

    this.proyectoService.publicarProyecto(this.nuevoProyecto).subscribe({
      next: (res) => {
        this.mensajeExito = '¡Proyecto publicado con éxito!';
        setTimeout(() => {
          this.router.navigate(['/cliente/dashboard']);
        }, 1500);
      },
      error: (err) => {
        this.mensajeError = err.error?.error || 'Ocurrió un error al publicar el proyecto.';
      }
    });
  }
}