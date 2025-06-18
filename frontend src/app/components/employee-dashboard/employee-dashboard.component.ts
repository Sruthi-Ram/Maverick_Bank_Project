import { Component,Renderer2 } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-employee-dashboard',
  templateUrl: './employee-dashboard.component.html',
  styleUrls: ['./employee-dashboard.component.css'] 
})
export class EmployeeDashboardComponent {
  isCollapsed = false;
  
   constructor(
        public renderer: Renderer2,
        public authService: AuthService,
        
        private router: Router
      ) {}
   logout() {
  this.authService.logout(); // clear tokens/session
  this.router.navigate(['/login']); // redirect 
}
}
