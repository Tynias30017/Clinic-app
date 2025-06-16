# Clinic App

System rejestracji wizyt pacjentów i zarządzania dostępnością lekarzy.

## Wymagania projektowe
- Rejestracja i logowanie pacjentów oraz lekarzy
- Pacjenci mogą umawiać wizyty
- Lekarze mogą zarządzać dostępnością
- Administrator może przeglądać statystyki i historię wizyt

## Technologie
- Java 17, Spring Boot, Spring Security, Spring Data JPA
- Hibernate, PostgreSQL, Flyway
- Docker, Maven
- Springdoc OpenAPI (Swagger UI)
- JUnit, JaCoCo

## Uruchomienie
1. Zbuduj projekt: `./mvnw clean package`
2. Uruchom w Dockerze: `docker-compose up --build`
3. Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Struktura bazy danych (ERD)

![ERD](doc/erd.png)

## Przykładowe migracje (Flyway)
Pliki migracji znajdują się w `src/main/resources/db/migration/`.

## Testy i pokrycie kodu
- Testy jednostkowe: `./mvnw test`
- Raport pokrycia: `target/site/jacoco/index.html`

## Screeny
- (Wstaw screeny z działania aplikacji i panelu Swagger UI)

## Autor
Grzegorz Niedojadło