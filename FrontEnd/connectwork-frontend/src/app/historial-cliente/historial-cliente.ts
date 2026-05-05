import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ClienteService } from '../services/cliente';

@Component({
  selector: 'app-historial-cliente',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './historial-cliente.html',
  styleUrl: './historial-cliente.css'
})
export class HistorialClienteComponent implements OnInit {
  historial: any[] = [];

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.clienteService.obtenerHistorialRecargas().subscribe({
      next: (data) => {
        console.log("Datos recibidos del backend:", data); 
        this.historial = data || [];
      },
      error: (err) => console.error("Error al cargar historial", err)
    });
  }
}