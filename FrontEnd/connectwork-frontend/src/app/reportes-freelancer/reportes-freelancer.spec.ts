import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportesFreelancerComponent } from './reportes-freelancer';

describe('ReportesFreelancerComponent', () => {
  let component: ReportesFreelancerComponent;
  let fixture: ComponentFixture<ReportesFreelancerComponent >;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportesFreelancerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportesFreelancerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
