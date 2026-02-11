# Pricing Service – Hexagonal Architecture

This project is a simple pricing service built with **Spring Boot** and **H2 in-memory database**, following a clean **hexagonal architecture** (domain / application / infrastructure).

## Requirements

- Java 17+
- Maven 3.8+

## Business requirements

The service exposes a REST endpoint to query product prices for a brand, given a date. It returns the price list, start/end dates, and final price. If multiple prices are valid, the one with the highest priority is selected. The database is initialized with sample data.

## Architecture

The project uses hexagonal architecture:

- **domain**: business model (`Price`) and port interface (`PriceRepository`)
- **application**: use case (`GetPriceUseCase`)
- **infrastructure**: persistence (H2 + Spring Data JPA), REST API, configuration

Domain and application layers are independent from Spring and persistence.

## Running the application

To start the app:

```bash
mvn spring-boot:run
```

The app runs on port `8080`.

## REST endpoint

**Path:** `GET /prices`

**Query parameters:**

- `applicationDate` (ISO-8601, e.g. `2020-06-14T10:00:00`)
- `productId` (e.g. `35455`)
- `brandId` (e.g. `1`)

**Example:**

```bash
curl "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"
```

**Example response:**

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 1,
  "startDate": "2020-06-14T00:00:00",
  "endDate": "2020-12-31T23:59:59",
  "price": 35.5,
  "currency": "EUR"
}
```

If no price is found, returns `404 Not Found`.

## Database

Uses **H2 in-memory** database.

- Schema: `schema.sql`
- Data: `data.sql`

H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pricingdb`, user: `sa`, password: empty)

## Tests

Unit tests cover the use case logic (`GetPriceUseCaseTest`), including selection by priority and cases with no applicable price. Controller tests (`PriceControllerTest`) check the main scenarios. Run tests with:

```bash
mvn test
```

## Project Structure

```
src/
  main/
    java/com/example/pricing/
      PricingApplication.java
      domain/model/Price.java
      domain/port/PriceRepository.java
      application/GetPriceUseCase.java
      infrastructure/configuration/BeanConfig.java
      infrastructure/persistence/PriceEntity.java
      infrastructure/persistence/SpringDataPriceRepository.java
      infrastructure/persistence/PriceRepositoryAdapter.java
      infrastructure/rest/PriceController.java
      infrastructure/rest/dto/PriceResponseDto.java
    resources/
      application.yml
      data.sql
      schema.sql
  test/
    java/com/example/pricing/application/GetPriceUseCaseTest.java
    java/com/example/pricing/infrastructure/rest/PriceControllerTest.java
```

## Design Decisions

**Hexagonal Architecture:**

- Domain layer: business logic, no infrastructure dependencies
- Application layer: use cases
- Infrastructure layer: adapters for persistence and REST

**Streams:**
Java Streams are used for filtering and selecting prices by priority.

**Immutability:**
The `Price` model is immutable for safety.

## Building the project

To build:

```bash
mvn clean package
```

Creates JAR in `target/prices-hexagonal-0.0.1-SNAPSHOT.jar`

## Author

Ricardo Jesús Cabrera Valero
