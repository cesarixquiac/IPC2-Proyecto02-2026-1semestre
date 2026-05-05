import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistorialFreelancer } from './historial-freelancer';

describe('HistorialFreelancer', () => {
  let component: HistorialFreelancer;
  let fixture: ComponentFixture<HistorialFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistorialFreelancer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistorialFreelancer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
