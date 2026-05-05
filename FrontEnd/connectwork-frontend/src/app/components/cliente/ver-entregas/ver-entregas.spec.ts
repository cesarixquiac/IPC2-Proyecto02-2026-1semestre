import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerEntregas } from './ver-entregas';

describe('VerEntregas', () => {
  let component: VerEntregas;
  let fixture: ComponentFixture<VerEntregas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerEntregas]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerEntregas);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
