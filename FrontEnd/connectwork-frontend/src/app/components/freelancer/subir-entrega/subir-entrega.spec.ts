import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubirEntrega } from './subir-entrega';

describe('SubirEntrega', () => {
  let component: SubirEntrega;
  let fixture: ComponentFixture<SubirEntrega>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubirEntrega]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubirEntrega);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
