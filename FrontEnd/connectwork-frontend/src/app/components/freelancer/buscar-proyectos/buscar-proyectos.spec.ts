import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscarProyectos } from './buscar-proyectos';

describe('BuscarProyectos', () => {
  let component: BuscarProyectos;
  let fixture: ComponentFixture<BuscarProyectos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuscarProyectos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuscarProyectos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
