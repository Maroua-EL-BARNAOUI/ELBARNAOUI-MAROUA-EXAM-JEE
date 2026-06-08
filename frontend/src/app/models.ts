export type StatutCredit = 'EN_COURS' | 'ACCEPTE' | 'REJETE';
export type TypeCredit = 'PERSONNEL' | 'IMMOBILIER' | 'PROFESSIONNEL';
export type TypeBien = 'APPARTEMENT' | 'MAISON' | 'LOCAL_COMMERCIAL';
export type TypeRemboursement = 'MENSUALITE' | 'REMBOURSEMENT_ANTICIPE';
export type Role = 'ROLE_CLIEN' | 'ROLE_EMPLOYE' | 'ROLE_ADMIN';

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  username: string;
  roles: Role[];
}

export interface Client {
  id?: number;
  nom: string;
  email: string;
}

export interface Credit {
  id?: number;
  dateDemande?: string;
  statut?: StatutCredit;
  dateAcceptation?: string;
  montant: number;
  dureeRemboursement: number;
  tauxInteret: number;
  clientId: number;
  clientNom?: string;
  typeCredit?: TypeCredit;
  motif?: string;
  typeBien?: TypeBien;
  raisonSociale?: string;
}

export interface Remboursement {
  id?: number;
  date: string;
  montant: number;
  type: TypeRemboursement;
  creditId: number;
}
