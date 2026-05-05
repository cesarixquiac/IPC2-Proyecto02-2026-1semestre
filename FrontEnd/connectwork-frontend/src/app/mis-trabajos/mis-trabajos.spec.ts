import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisTrabajos } from './mis-trabajos';

describe('MisTrabajos', () => {
  let component: MisTrabajos;
  let fixture: ComponentFixture<MisTrabajos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisTrabajos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisTrabajos);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
