import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluarEntregas } from './evaluar-entregas';

describe('EvaluarEntregas', () => {
  let component: EvaluarEntregas;
  let fixture: ComponentFixture<EvaluarEntregas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluarEntregas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EvaluarEntregas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
