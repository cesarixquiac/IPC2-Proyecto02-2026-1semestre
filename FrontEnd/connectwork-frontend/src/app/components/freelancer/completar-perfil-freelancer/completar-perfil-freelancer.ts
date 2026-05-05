import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FreelancerService } from '../../../services/freelancer'; 
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-completar-perfil-freelancer',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './completar-perfil-freelancer.html'
})
export class CompletarPerfilFreelancerComponent implements OnInit {

  perfil = {
    biografia: '',
    nivelExperiencia: '',
    tarifaHora: null,
    habilidades: [] as number[]
  };

  listaHabilidades: any[] = [];

  constructor(
    private freelancerService: FreelancerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarHabilidades();
  }

  cargarHabilidades() {
    this.freelancerService.obtenerHabilidades().subscribe({
      next: (res: any) => this.listaHabilidades = res,
      error: (err) => console.error("Error cargando habilidades", err)
    });
  }

  toggleHabilidad(idHabilidad: number, event: any) {
    if (event.target.checked) {
      this.perfil.habilidades.push(idHabilidad);
    } else {
      this.perfil.habilidades = this.perfil.habilidades.filter(id => id !== idHabilidad);
    }
  }

  guardarPerfil() {
    if (!this.perfil.biografia || !this.perfil.nivelExperiencia || !this.perfil.tarifaHora || this.perfil.habilidades.length === 0) {
      alert("Por favor, completa todos los campos y selecciona al menos una habilidad.");
      return;
    }

    this.freelancerService.completarPerfil(this.perfil).subscribe({
      next: (res) => {
        alert("¡Perfil completado con éxito! Bienvenido a ConnectWork.");
        this.router.navigate(['/freelancer/dashboard']); 
      },
      error: (err) => alert("Error al guardar el perfil: " + err.error?.error)
    });
  }
}