import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importante para usar ngModel
import { FreelancerService } from '../services/freelancer';

@Component({
  selector: 'app-reportes-freelancer',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes-freelancer.html'
})
export class ReportesFreelancerComponent implements OnInit {
  // Variables para las fechas
  fechaInicio: string = '';
  fechaFin: string = '';

  // Arreglos para guardar los datos
  topCategorias: any[] = [];
  contratosCompletados: any[] = [];
  propuestasEnviadas: any[] = [];

  constructor(private freelancerService: FreelancerService) { }

  ngOnInit(): void {
    // Este no necesita fechas, lo cargamos de inmediato
    this.cargarTopCategorias();
  }

  cargarTopCategorias() {
    this.freelancerService.obtenerReporteTopCategorias().subscribe({
      next: (data: any) => this.topCategorias = data || [],
      error: (err: any) => console.error("Error al cargar Top Categorías", err)
    });
  }

  generarReportesFechas() {
    if (!this.fechaInicio || !this.fechaFin) {
      alert("Por favor, selecciona ambas fechas.");
      return;
    }

    // Cargar reporte de contratos
    this.freelancerService.obtenerReporteContratosCompletados(this.fechaInicio, this.fechaFin).subscribe({
      next: (data: any) => this.contratosCompletados = data || [],
      error: (err: any) => console.error("Error al cargar Contratos", err)
    });

    // Cargar reporte de propuestas
    this.freelancerService.obtenerReportePropuestasEnviadas(this.fechaInicio, this.fechaFin).subscribe({
      next: (data: any) => this.propuestasEnviadas = data || [],
      error: (err: any) => console.error("Error al cargar Propuestas", err)
    });
  }

  imprimirReporte() {
    window.print();
  }
}