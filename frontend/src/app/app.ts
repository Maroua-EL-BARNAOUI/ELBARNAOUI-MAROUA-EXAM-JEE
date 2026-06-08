import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from './auth.service';
import { BankCreditService } from './bank-credit.service';
import { AuthRequest, AuthResponse, Client, Credit, Remboursement, Role, StatutCredit, TypeBien, TypeCredit, TypeRemboursement } from './models';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  clients: Client[] = [];
  credits: Credit[] = [];
  remboursements: Remboursement[] = [];

  selectedCredit?: Credit;
  currentUser: AuthResponse | null = null;
  selectedStatut: StatutCredit | '' = '';
  mensualite?: number;
  message = '';
  error = '';
  loading = false;

  readonly statuts: StatutCredit[] = ['EN_COURS', 'ACCEPTE', 'REJETE'];
  readonly typesCredit: TypeCredit[] = ['PERSONNEL', 'IMMOBILIER', 'PROFESSIONNEL'];
  readonly typesBien: TypeBien[] = ['APPARTEMENT', 'MAISON', 'LOCAL_COMMERCIAL'];
  readonly typesRemboursement: TypeRemboursement[] = ['MENSUALITE', 'REMBOURSEMENT_ANTICIPE'];

  clientForm: Client = {
    nom: '',
    email: ''
  };

  creditForm: Credit = this.emptyCreditForm();

  remboursementForm: Remboursement = this.emptyRemboursementForm();

  loginForm: AuthRequest = {
    username: 'admin',
    password: 'admin123'
  };

  constructor(
    private readonly bankCreditService: BankCreditService,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser) {
      this.refreshData();
    }
  }

  login(): void {
    this.clearFeedback();
    this.authService.login(this.loginForm).subscribe({
      next: user => {
        this.currentUser = user;
        this.message = 'Connexion reussie.';
        this.refreshData();
      },
      error: () => this.handleError('Identifiants invalides.')
    });
  }

  logout(): void {
    this.authService.logout();
    this.currentUser = null;
    this.clients = [];
    this.credits = [];
    this.remboursements = [];
    this.selectedCredit = undefined;
    this.message = '';
    this.error = '';
  }

  refreshData(): void {
    this.loading = true;
    this.error = '';
    if (!this.hasAnyRole(['ROLE_ADMIN', 'ROLE_EMPLOYE'])) {
      this.loadCredits();
      return;
    }
    this.bankCreditService.listClients().subscribe({
      next: clients => {
        this.clients = clients;
        this.loadCredits();
      },
      error: () => this.handleError('Impossible de charger les clients.')
    });
  }

  loadCredits(): void {
    this.bankCreditService.listCredits(this.selectedStatut).subscribe({
      next: credits => {
        this.credits = credits;
        this.loading = false;
      },
      error: () => this.handleError('Impossible de charger les credits.')
    });
  }

  createClient(): void {
    if (!this.hasRole('ROLE_ADMIN')) {
      return;
    }
    this.clearFeedback();
    this.bankCreditService.saveClient(this.clientForm).subscribe({
      next: () => {
        this.clientForm = { nom: '', email: '' };
        this.message = 'Client ajoute avec succes.';
        this.refreshData();
      },
      error: () => this.handleError('Creation du client impossible.')
    });
  }

  createCredit(): void {
    this.clearFeedback();
    const request = this.creditForm.typeCredit === 'IMMOBILIER'
      ? this.bankCreditService.saveCreditImmobilier(this.creditForm)
      : this.creditForm.typeCredit === 'PROFESSIONNEL'
        ? this.bankCreditService.saveCreditProfessionnel(this.creditForm)
        : this.bankCreditService.saveCreditPersonnel(this.creditForm);

    request.subscribe({
      next: () => {
        this.creditForm = this.emptyCreditForm();
        this.message = 'Credit enregistre avec succes.';
        this.loadCredits();
      },
      error: () => this.handleError('Creation du credit impossible.')
    });
  }

  accepterCredit(credit: Credit): void {
    if (!this.hasAnyRole(['ROLE_ADMIN', 'ROLE_EMPLOYE'])) {
      return;
    }
    if (!credit.id) {
      return;
    }
    this.clearFeedback();
    this.bankCreditService.accepterCredit(credit.id).subscribe({
      next: () => {
        this.message = 'Credit accepte.';
        this.loadCredits();
      },
      error: () => this.handleError('Acceptation du credit impossible.')
    });
  }

  rejeterCredit(credit: Credit): void {
    if (!this.hasAnyRole(['ROLE_ADMIN', 'ROLE_EMPLOYE'])) {
      return;
    }
    if (!credit.id) {
      return;
    }
    this.clearFeedback();
    this.bankCreditService.rejeterCredit(credit.id).subscribe({
      next: () => {
        this.message = 'Credit rejete.';
        this.loadCredits();
      },
      error: () => this.handleError('Rejet du credit impossible.')
    });
  }

  selectCredit(credit: Credit): void {
    if (!credit.id) {
      return;
    }
    this.selectedCredit = credit;
    this.remboursementForm = this.emptyRemboursementForm(credit.id);
    this.bankCreditService.listRemboursements(credit.id).subscribe({
      next: remboursements => this.remboursements = remboursements,
      error: () => this.handleError('Chargement des remboursements impossible.')
    });
    this.bankCreditService.calculerMensualite(credit.id).subscribe({
      next: mensualite => this.mensualite = mensualite,
      error: () => this.mensualite = undefined
    });
  }

  createRemboursement(): void {
    if (!this.hasAnyRole(['ROLE_ADMIN', 'ROLE_EMPLOYE'])) {
      return;
    }
    this.clearFeedback();
    this.bankCreditService.saveRemboursement(this.remboursementForm).subscribe({
      next: () => {
        this.message = 'Remboursement ajoute avec succes.';
        if (this.selectedCredit?.id) {
          this.selectCredit(this.selectedCredit);
        }
      },
      error: () => this.handleError('Ajout du remboursement impossible.')
    });
  }

  formatType(value?: string): string {
    return value ? value.replace(/_/g, ' ') : '-';
  }

  hasRole(role: Role): boolean {
    return this.authService.hasRole(role);
  }

  hasAnyRole(roles: Role[]): boolean {
    return this.authService.hasAnyRole(roles);
  }

  private emptyCreditForm(): Credit {
    return {
      clientId: 0,
      typeCredit: 'PERSONNEL',
      montant: 10000,
      dureeRemboursement: 24,
      tauxInteret: 4.5,
      motif: '',
      typeBien: 'APPARTEMENT',
      raisonSociale: ''
    };
  }

  private emptyRemboursementForm(creditId = 0): Remboursement {
    return {
      creditId,
      date: new Date().toISOString().slice(0, 10),
      montant: 0,
      type: 'MENSUALITE'
    };
  }

  private clearFeedback(): void {
    this.message = '';
    this.error = '';
  }

  private handleError(message: string): void {
    this.loading = false;
    this.error = message;
  }
}
