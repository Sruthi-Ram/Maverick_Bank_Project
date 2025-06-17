import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CustomerViewProfileComponent } from './customer-view-profile.component';
import { CustomerService } from '../../services/customer.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('CustomerViewProfileComponent', () => {
  let component: CustomerViewProfileComponent;
  let fixture: ComponentFixture<CustomerViewProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomerViewProfileComponent],
      imports: [HttpClientTestingModule],
      providers: [
        CustomerService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => '101' // Mock userId
              }
            }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CustomerViewProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
