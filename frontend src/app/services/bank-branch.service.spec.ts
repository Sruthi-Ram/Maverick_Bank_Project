import { TestBed } from '@angular/core/testing';
import { BankBranchService } from './bank-branch.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BankBranchService', () => {
  let service: BankBranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule] // Required for HttpClient
    });
    service = TestBed.inject(BankBranchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
