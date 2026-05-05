import { Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login';
import { DashboardClienteComponent } from './components/cliente/dashboard-cliente/dashboard-cliente';
import { DashboardFreelancerComponent } from './components/freelancer/dashboard-freelancer/dashboard-freelancer';
import { authGuard } from './guards/auth-guard'
import { PublicarProyectoComponent } from './components/cliente/publicar-proyecto/publicar-proyecto';
import { RegistroComponent } from './components/auth/registro/registro';
import { CompletarPerfilComponent } from './components/cliente/completar-perfil-cliente/completar-perfil-cliente';
import { VerPropuestasComponent } from './components/cliente/ver-propuestas/ver-propuestas';
import { VerEntregasComponent } from './components/cliente/ver-entregas/ver-entregas';
import { CompletarPerfilFreelancerComponent } from './components/freelancer/completar-perfil-freelancer/completar-perfil-freelancer';
import { MisTrabajosComponent } from './mis-trabajos/mis-trabajos';
import { HistorialFreelancerComponent } from './historial-freelancer/historial-freelancer';
import { HistorialClienteComponent } from './historial-cliente/historial-cliente';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  { path: 'cliente/dashboard', component: DashboardClienteComponent, canActivate: [authGuard] },
  { path: 'freelancer/dashboard', component: DashboardFreelancerComponent, canActivate: [authGuard] },
  { path: 'cliente/publicar-proyecto', component: PublicarProyectoComponent, canActivate: [authGuard] },
    { path: 'cliente/completar-perfil', component: CompletarPerfilComponent, canActivate: [authGuard] },
    { path: 'cliente/proyectos/:id/propuestas', component: VerPropuestasComponent, canActivate: [authGuard] },
    { path: 'cliente/proyectos/:id/entregas', component: VerEntregasComponent, canActivate: [authGuard] } ,
    { path: 'freelancer/completar-perfil', component: CompletarPerfilFreelancerComponent,  canActivate: [authGuard] },
    { path: 'freelancer/mis-trabajos', component: MisTrabajosComponent, canActivate: [authGuard] }, 
    { path: 'cliente/historial', component: HistorialClienteComponent, canActivate: [authGuard] },
{ path: 'freelancer/historial', component: HistorialFreelancerComponent, canActivate: [authGuard] },
  { path: 'registro', component: RegistroComponent },
  { path: '**', redirectTo: 'login' },
 
];