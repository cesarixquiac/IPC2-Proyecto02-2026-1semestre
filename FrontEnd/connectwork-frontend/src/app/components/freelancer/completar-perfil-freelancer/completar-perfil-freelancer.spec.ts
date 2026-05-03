import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompletarPerfilFreelancer } from './completar-perfil-freelancer';

describe('CompletarPerfilFreelancer', () => {
  let component: CompletarPerfilFreelancer;
  let fixture: ComponentFixture<CompletarPerfilFreelancer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompletarPerfilFreelancer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CompletarPerfilFreelancer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
