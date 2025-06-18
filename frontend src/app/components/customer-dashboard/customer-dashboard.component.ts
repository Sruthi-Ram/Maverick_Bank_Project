import { Component, OnInit, Renderer2 } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer';

declare var bootstrap: any;

@Component({
  selector: 'app-customer-dashboard',
  templateUrl: './customer-dashboard.component.html',
  styleUrls: ['./customer-dashboard.component.css']
})
export class CustomerDashboardComponent implements OnInit {
  isCollapsed: boolean = false;
  userId: number = 0;
  customer?: Customer;
 

  constructor(
    public renderer: Renderer2,
    public authService: AuthService,
    private customerService: CustomerService,
    private router: Router
  ) {}

  logout() {
  this.authService.logout(); // clear tokens/session
  this.router.navigate(['/login']); // redirect
}


  ngOnInit() {
    
    const user = this.authService.getCurrentUser();
    if (user && user.userId) {
      this.userId = user.userId;

      
      this.customerService.getCustomerByUserId(this.userId).subscribe({
        next: (data) => {
          this.customer = data;
          console.log('Customer loaded:', this.customer);
        },
        error: (err) => {
          console.error('Failed to load customer:', err);
        }
      });
    } else {
      console.warn('No logged-in user or userId not found.');
    }

    
    const toggleBtn = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');

    toggleBtn?.addEventListener('click', () => {
      this.isCollapsed = !this.isCollapsed;
      sidebar?.classList.toggle('collapsed', this.isCollapsed);
    });
  }
}
