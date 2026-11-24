import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { ProfitLossService } from './profit-loss.service';

describe('ProfitLossService', () => {
  let service: ProfitLossService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(ProfitLossService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
