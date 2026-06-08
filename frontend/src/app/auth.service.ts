import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthRequest, AuthResponse, Role } from './models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly tokenKey = 'bank-credit-token';
  private readonly userKey = 'bank-credit-user';
  private readonly apiUrl = '/api/auth';

  constructor(private readonly http: HttpClient) {}

  login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => this.saveSession(response))
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getCurrentUser(): AuthResponse | null {
    const rawUser = localStorage.getItem(this.userKey);
    return rawUser ? JSON.parse(rawUser) as AuthResponse : null;
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  hasRole(role: Role): boolean {
    return this.getCurrentUser()?.roles.includes(role) ?? false;
  }

  hasAnyRole(roles: Role[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  private saveSession(response: AuthResponse): void {
    localStorage.setItem(this.tokenKey, response.accessToken);
    localStorage.setItem(this.userKey, JSON.stringify(response));
  }
}
