import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loginUrl = 'http://localhost:8080/api/v1/users/login';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
  return this.http.post<any>(this.loginUrl, { username, password }).pipe(
    tap(response => {
      if (response.token && response.user) {
       
        localStorage.setItem('token', response.token);

       
        const currentUser = {
          ...response.user,        
          token: response.token     
        };
        this.setCurrentUser(currentUser);
      }
    })
  );
}


  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    
  }

  setCurrentUser(user: any): void {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  getCurrentUser(): any {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
