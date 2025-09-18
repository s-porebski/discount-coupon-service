# Discount Coupon Service

A REST API for managing discount coupons, developed as a recruitment task.

## Tech Stack

- Java 17, Spring Boot 3, Maven
- Spring Data JPA / Hibernate, H2
- JUnit 5, Mockito, MockMvc

## Run Locally

1. **Build & Test:**
   ```bash
   ./mvnw clean install
   ```
2. **Start the Application:**
   ```bash
   ./mvnw spring-boot:run
   ```

## API Usage

### 1. Create Coupon

`POST /api/v1/coupons`

**Example Request:**

```sh
curl -X POST http://localhost:8080/api/v1/coupons \
  -H "Content-Type: application/json" \
  -d '{"code":"WIOSNA25","maxUses":100,"countryCode":"PL"}'
```

### 2. Use Coupon

`POST /api/v1/coupons/usage`

**Example Request:**

```sh
curl -X POST http://localhost:8080/api/v1/coupons/usage \
  -H "Content-Type: application/json" \
  -d '{"code":"WIOSNA25"}'
```

## Error handling (example)

When a coupon is invalid for the user's country, the API may respond with 422 Unprocessable Entity:

```json
{
  "error": "Unprocessable Entity",
  "message": "Coupon is not valid in your country.",
  "path": "/api/v1/coupons/usage",
  "status": 422,
  "timestamp": 1758225776639
}
```

## Considerations

- **Database**:
    - The project uses **H2 (in-memory)** by default for simplicity.
    - However, the application is based on JPA/JPQL and can work with different relational databases.
    - Currently, the primary key strategy is `IDENTITY`, which may require adjustments when using e.g. Oracle.

- **Data integrity**:
    - Coupon codes are normalized to **UPPERCASE** on application level.
    - To enforce additional integrity, a **unique constraint** on the `code` column could be defined in the database.

- **External API**:
    - IP Geolocation API is subject to rate limiting. To handle this in a multi-instance environment, a distributed rate
      limiter would be a necessary.