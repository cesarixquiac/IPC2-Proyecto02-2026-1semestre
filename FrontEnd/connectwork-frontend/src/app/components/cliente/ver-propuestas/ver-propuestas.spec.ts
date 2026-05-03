import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerPropuestas } from './ver-propuestas';

describe('VerPropuestas', () => {
  let component: VerPropuestas;
  let fixture: ComponentFixture<VerPropuestas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerPropuestas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerPropuestas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
