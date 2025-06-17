import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'maverick_bank';
  showNav = true;

  constructor(public authService: AuthService, private router: Router) {
    // Toggle nav bar visibility
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.showNav = !(event.url.includes('/login') || event.url.includes('/register'));
      }
    });
  }

  ngOnInit(): void {
    const user = localStorage.getItem('currentUser');
    const token = localStorage.getItem('token');

    if (user && token) {
      // Restore session in AuthService
      this.authService.setCurrentUser(JSON.parse(user));
    } else {
      // Optional: only redirect if not already on /login
      if (!this.router.url.includes('/login')) {
        this.router.navigate(['/login']);
      }
    }
  }
}
