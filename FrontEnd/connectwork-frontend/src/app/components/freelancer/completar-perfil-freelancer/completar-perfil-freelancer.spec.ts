import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarPerfilFreelancerComponent } from './completar-perfil-freelancer';

describe('CompletarPerfilFreelancerComponent', () => {
  let component: CompletarPerfilFreelancerComponent;
  let fixture: ComponentFixture<CompletarPerfilFreelancerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletarPerfilFreelancerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompletarPerfilFreelancerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
