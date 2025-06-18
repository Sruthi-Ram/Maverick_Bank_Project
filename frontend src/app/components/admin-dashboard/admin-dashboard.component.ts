import { Component,Renderer2} from '@angular/core';

import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
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
