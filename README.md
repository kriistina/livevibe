# LiveVibe Spring Boot aplikacija

## Opis
LiveVibe je Spring Boot aplikacija za prodaju i pregled koncerata s MySQL bazom podataka, pokrenuta u Docker kontejnerima i spremna za deployment na Oracle Cloud platformu.

## Tehnologije
- Java 17
- Spring Boot
- MySQL 8
- Docker, Docker Compose

---

## Kako pokrenuti aplikaciju lokalno ili na serveru (npr. Oracle Cloud VM)

### Preduvjeti
- Instalirani Docker i Docker Compose
- Kloniran repozitorij sa svim fajlovima (`Dockerfile`, `docker-compose.yml`, `application.properties` i izvornim kodom)

### Koraci za pokretanje

1. SSH na server (ili lokalno ako testiraš):
   ```bash
   ssh -i ~/.ssh/id_rsa ubuntu@<IP-adresa-servera>
