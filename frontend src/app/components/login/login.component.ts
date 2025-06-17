import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage = '';
  successMessage = '';
  returnUrl = '/';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', Validators.required]
    });

    
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

   
    this.route.queryParams.subscribe(params => {
      if (params['registered'] === 'true') {
        this.successMessage = 'Registration successful. Please login.';
      }
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    const { username, password } = this.loginForm.value;

    this.authService.login(username, password).subscribe({
      next: (response) => {
        if (response && response.token) {
          
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response));
          localStorage.setItem('roleId', response.roleId.toString());

          this.authService.setCurrentUser(response); 

          
          let redirectRoute = '/';
          switch (response.roleId) {
            case 1:
              redirectRoute = '/customer-dashboard';
              break;
            case 2:
              redirectRoute = '/employee-dashboard';
              break;
            case 3:
              redirectRoute = '/admin-dashboard';
              break;
          }

          this.router.navigateByUrl(redirectRoute);
        } else {
          this.errorMessage = 'Invalid username or password.';
        }
      },
      error: () => {
        this.errorMessage = 'Login failed. Please try again later.';
      }
    });
  }
}
