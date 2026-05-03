import { TestBed } from '@angular/core/testing';

import { Entrega } from './entrega';

describe('Entrega', () => {
  let service: Entrega;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Entrega);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
