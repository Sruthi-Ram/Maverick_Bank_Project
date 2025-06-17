import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ViewLoansComponent } from './view-loans.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LoanService } from '../../services/loan.service'; // adjust path if needed

describe('ViewLoansComponent', () => {
  let component: ViewLoansComponent;
  let fixture: ComponentFixture<ViewLoansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewLoansComponent],
      imports: [HttpClientTestingModule], 
      providers: [LoanService]            
    }).compileComponents();

    fixture = TestBed.createComponent(ViewLoansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
