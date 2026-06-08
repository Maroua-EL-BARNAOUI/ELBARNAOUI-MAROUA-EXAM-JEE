# Gestion des Crédits Bancaires - ENSET Mohammedia

## Description
Application Web JEE de gestion de crédits bancaires développée avec Spring Boot et Angular.

## Technologies utilisées
- **Backend** : Spring Boot 3, Spring Data JPA, Spring Security, JWT
- **Frontend** : Angular
- **Base de données** : H2 (dev), MySQL (prod)
- **Documentation API** : Swagger / OpenAPI 3

## Prérequis
- Java 21
- Maven
- Node.js + Angular CLI
- IntelliJ IDEA (recommandé)

## Installation et lancement

### Backend

1. Cloner le repository
git clone https://github.com/VotreNom-VotrePrenom-Exam-JEE.git

2. Ouvrir le projet dans IntelliJ IDEA

3. Lancer l'application
- Ouvrir `ExamApplication.java`
- Cliquer sur le bouton ▶

4. L'application démarre sur `http://localhost:8080`

### Frontend

1. Aller dans le dossier frontend
cd frontend

2. Installer les dépendances
npm install

3. Lancer l'application
ng serve

4. Ouvrir `http://localhost:4200`

## Accès aux interfaces

| Interface | URL |
|-----------|-----|
| API REST | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |

## Configuration H2
- **JDBC URL** : `jdbc:h2:mem:creditdb`
- **Username** : `SA`
- **Password** : *(vide)*

## Comptes de test

| Utilisateur | Mot de passe | Rôle |
|-------------|--------------|------|
| admin | admin123 | ROLE_ADMIN |
| employe | employe123 | ROLE_EMPLOYE |
| client | client123 | ROLE_CLIEN |

## Authentification JWT

1. Appeler `POST /api/auth/login` avec :
json
{
  "username": "admin",
  "password": "admin123"
}

2. Récupérer le `accessToken` dans la réponse

3. Ajouter dans les headers de chaque requête :
Authorization: Bearer <accessToken>

## Endpoints principaux

### Authentification
- `POST /api/auth/login` — Connexion
- `POST /api/auth/register` — Inscription

### Clients
- `GET /api/clients` — Lister tous les clients
- `GET /api/clients/{id}` — Consulter un client
- `POST /api/clients` — Créer un client
- `PUT /api/clients/{id}` — Modifier un client
- `GET /api/clients/{id}/credits` — Crédits d'un client

### Crédits
- `GET /api/credits` — Lister tous les crédits
- `GET /api/credits/{id}` — Consulter un crédit
- `POST /api/credits/personnels` — Créer un crédit personnel
- `POST /api/credits/immobiliers` — Créer un crédit immobilier
- `POST /api/credits/professionnels` — Créer un crédit professionnel
- `POST /api/credits/{id}/acceptation` — Accepter un crédit
- `POST /api/credits/{id}/rejet` — Rejeter un crédit

### Remboursements
- `GET /api/credits/{id}/remboursements` — Lister les remboursements
- `POST /api/remboursements` — Ajouter un remboursement

## Structure du projet
src/
├── main/
│   ├── java/org/sid/exam/
│   │   ├── config/          # Configuration Security, Swagger
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entities/        # Entités JPA
│   │   ├── exceptions/      # Gestion des erreurs
│   │   ├── mappers/         # Mappers DTO <-> Entités
│   │   ├── repositories/    # Spring Data Repositories
│   │   ├── security/        # JWT Filter, UserDetailsService
│   │   ├── services/        # Couche métier
│   │   └── web/             # REST Controllers
│   └── resources/
│       └── application.properties



- 
