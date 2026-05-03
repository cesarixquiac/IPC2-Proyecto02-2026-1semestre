import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarPerfilCliente } from './completar-perfil-cliente';

describe('CompletarPerfilCliente', () => {
  let component: CompletarPerfilCliente;
  let fixture: ComponentFixture<CompletarPerfilCliente>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletarPerfilCliente]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompletarPerfilCliente);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
