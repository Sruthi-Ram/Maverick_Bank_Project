import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ApplyLoanComponent } from './apply-loan.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LoanService } from '../../services/loan.service';

describe('ApplyLoanComponent', () => {
  let component: ApplyLoanComponent;
  let fixture: ComponentFixture<ApplyLoanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ApplyLoanComponent],
      imports: [
        FormsModule, // ✅ for ngForm
        HttpClientTestingModule // ✅ for HttpClient
      ],
      providers: [
        LoanService,
        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: of({ loanId: '123' }),
            snapshot: {
              queryParamMap: {
                get: (key: string) => '123'
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ApplyLoanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
