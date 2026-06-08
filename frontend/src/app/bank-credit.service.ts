import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client, Credit, Remboursement, StatutCredit } from './models';

@Injectable({
  providedIn: 'root'
})
export class BankCreditService {
  private readonly apiUrl = '/api';

  constructor(private readonly http: HttpClient) {}

  listClients(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.apiUrl}/clients`);
  }

  saveClient(client: Client): Observable<Client> {
    return this.http.post<Client>(`${this.apiUrl}/clients`, client);
  }

  listCredits(statut?: StatutCredit | ''): Observable<Credit[]> {
    const params = statut ? new HttpParams().set('statut', statut) : undefined;
    return this.http.get<Credit[]>(`${this.apiUrl}/credits`, { params });
  }

  saveCreditPersonnel(credit: Credit): Observable<Credit> {
    return this.http.post<Credit>(`${this.apiUrl}/credits/personnels`, credit);
  }

  saveCreditImmobilier(credit: Credit): Observable<Credit> {
    return this.http.post<Credit>(`${this.apiUrl}/credits/immobiliers`, credit);
  }

  saveCreditProfessionnel(credit: Credit): Observable<Credit> {
    return this.http.post<Credit>(`${this.apiUrl}/credits/professionnels`, credit);
  }

  accepterCredit(creditId: number): Observable<Credit> {
    return this.http.post<Credit>(`${this.apiUrl}/credits/${creditId}/acceptation`, {});
  }

  rejeterCredit(creditId: number): Observable<Credit> {
    return this.http.post<Credit>(`${this.apiUrl}/credits/${creditId}/rejet`, {});
  }

  calculerMensualite(creditId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/credits/${creditId}/mensualite`);
  }

  listRemboursements(creditId: number): Observable<Remboursement[]> {
    return this.http.get<Remboursement[]>(`${this.apiUrl}/credits/${creditId}/remboursements`);
  }

  saveRemboursement(remboursement: Remboursement): Observable<Remboursement> {
    return this.http.post<Remboursement>(`${this.apiUrl}/remboursements`, remboursement);
  }
}
