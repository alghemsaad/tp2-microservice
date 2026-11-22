# compte-service (TP2 – Exercice 2) — port 8083

Base MySQL `db_comptes` (créée automatiquement). Nécessite le security-service (8080) pour obtenir un token.

| Méthode | URL | Accès |
|---|---|---|
| POST   | /api/comptes                 | ADMIN |
| PUT    | /api/comptes/{id}            | ADMIN |
| DELETE | /api/comptes/{id}            | ADMIN |
| GET    | /api/comptes                 | ADMIN |
| GET    | /api/comptes/{id}            | authentifié |
| GET    | /api/comptes/{id}/solde      | authentifié |
| POST   | /api/comptes/{id}/credit     | authentifié — body `{"montant": 500}` |
| POST   | /api/comptes/{id}/debit      | authentifié — body `{"montant": 200}` (400 si solde insuffisant) |

Création / modification : `{"nom":"Alaoui Yassine","tel":"0612345678","solde":1000}`

Jeu de données :
```sql
USE db_comptes;
INSERT INTO compte (nom, tel, solde) VALUES
('Alaoui Yassine','0612345678',5000.00),
('Bennani Salma','0623456789',12000.50),
('Tazi Imane','0634567890',300.00);
```
