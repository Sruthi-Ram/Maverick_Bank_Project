import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MyLoansComponent } from './my-loans.component';
import { LoanService } from '../../services/loan.service';
import { ActivatedRoute } from '@angular/router';

describe('MyLoansComponent', () => {
  let component: MyLoansComponent;
  let fixture: ComponentFixture<MyLoansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MyLoansComponent],
      imports: [HttpClientTestingModule],
      providers: [
        LoanService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => {
                  if (key === 'customerId') return '101';
                  return null;
                }
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(MyLoansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
