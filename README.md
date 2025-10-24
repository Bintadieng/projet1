# Banking API - Spring Boot

API REST pour la gestion bancaire avec Spring Boot, Spring Security et PostgreSQL (Supabase).

## Fonctionnalités

### Endpoints disponibles

| Méthode | Endpoint | Description | Rôles autorisés |
|---------|----------|-------------|-----------------|
| GET | `/clients` | Afficher tous les clients | USER, ADMIN |
| GET | `/solde?numeroCompte=xxx` | Afficher le solde d'un compte | USER, ADMIN |
| POST | `/create` | Créer un compte bancaire | ADMIN |
| POST | `/transfere` | Effectuer un transfert | USER |

## Authentification

L'API utilise l'authentification Basic Auth avec deux utilisateurs:

- **User**: `user` / `user123` (rôle: USER)
- **Admin**: `admin` / `admin123` (rôles: ADMIN, USER)

## Lancement de l'application

```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## Tests des endpoints

### 1. GET /clients (USER ou ADMIN)

```bash
curl -u user:user123 http://localhost:8080/clients
```

Réponse:
```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@email.com",
    "telephone": "0612345678"
  }
]
```

### 2. GET /solde (USER ou ADMIN)

```bash
curl -u user:user123 "http://localhost:8080/solde?numeroCompte=FR7630001007941234567890185"
```

Réponse:
```json
{
  "compteId": "c3d4e5f6-a7b8-9012-cdef-123456789012",
  "numeroCompte": "FR7630001007941234567890185",
  "solde": 1500.00,
  "typeCompte": "courant"
}
```

### 3. POST /create (ADMIN uniquement)

```bash
curl -u admin:admin123 -X POST http://localhost:8080/create \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCompte": "FR7630006000011234567890189",
    "clientId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "soldeInitial": 1000.00,
    "typeCompte": "epargne"
  }'
```

Réponse: 201 CREATED

### 4. POST /transfere (USER uniquement)

```bash
curl -u user:user123 -X POST http://localhost:8080/transfere \
  -H "Content-Type: application/json" \
  -d '{
    "numeroCompteSource": "FR7630001007941234567890185",
    "numeroCompteDestination": "FR7630004000031234567890143",
    "montant": 200.00,
    "description": "Paiement loyer"
  }'
```

Réponse: 200 OK

## Codes de statut HTTP

| Code | Signification | Exemple |
|------|---------------|---------|
| 200 | OK | Requête réussie |
| 201 | Created | Ressource créée avec succès |
| 400 | Bad Request | Validation échouée ou solde insuffisant |
| 401 | Unauthorized | Authentification requise |
| 403 | Forbidden | Accès refusé (rôle insuffisant) |
| 404 | Not Found | Ressource non trouvée |
| 500 | Internal Server Error | Erreur serveur |

## Structure des erreurs

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Compte non trouvé: FR763000...",
  "timestamp": "2025-10-24T10:30:00",
  "path": "/solde"
}
```

## Données de test

Deux clients et comptes sont disponibles:

**Client 1**: Jean Dupont
- Compte: FR7630001007941234567890185
- Solde initial: 1500.00 EUR

**Client 2**: Marie Martin
- Compte: FR7630004000031234567890143
- Solde initial: 2500.00 EUR

## Technologies utilisées

- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- PostgreSQL (Supabase)
- Lombok
- Jakarta Validation
