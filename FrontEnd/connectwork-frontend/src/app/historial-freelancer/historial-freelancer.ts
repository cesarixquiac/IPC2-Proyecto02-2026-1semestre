import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FreelancerService } from '../services/freelancer';

@Component({
  selector: 'app-historial-freelancer',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './historial-freelancer.html',
  styleUrl: './historial-freelancer.css'
})
export class HistorialFreelancerComponent implements OnInit {
  historial: any[] = [];

  constructor(private freelancerService: FreelancerService) {}

  ngOnInit(): void {
    this.freelancerService.obtenerHistorialGanancias().subscribe({
      next: (data) => this.historial = data,
      error: (err) => console.error("Error al cargar ganancias", err)
    });
  }
}