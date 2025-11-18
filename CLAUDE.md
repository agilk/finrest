# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Finrest is a Spring Boot 1.5.7 REST API for personal finance management with multi-currency support, transaction tracking, and currency conversion capabilities. The application uses:
- Java 8
- Maven build system
- MySQL database with JPA/Hibernate
- Embedded Jetty server
- Netflix Hystrix for circuit breaker patterns

## Common Commands

### Build and Run
```bash
# Clean and package the application
mvn clean package

# Run the application
mvn spring-boot:run

# Or run the packaged JAR
java -jar target/Finamance-1.0.0.jar
```

### Testing
Note: This project currently has no automated tests. When adding tests, use:
```bash
mvn test              # Run all tests
mvn test -Dtest=ClassName    # Run specific test class
```

## Architecture

### Request/Response Pattern
All API endpoints follow a consistent wrapper pattern:
- **Request object** (`az.kerimov.fin.finance.pojo.Request`) - Contains sessionKey and all request parameters
- **Response object** (`az.kerimov.fin.finance.pojo.Response`) - Contains either Data or Error
- Every controller method is annotated with `@HystrixCommand` for fault tolerance
- All requests/responses are logged to the database via the Log entity

### Authentication & Session Management
- Session-based authentication using MD5-hashed session keys
- SessionKey must be included in all API requests (except login)
- Session validation happens in the service layer via `getUserBySessionKey()`
- Active sessions are tracked in the Session entity

### Three-Layer Architecture
1. **Controller Layer** (`FinController.java`)
   - Single controller with 35 REST endpoints
   - All methods follow pattern: log request → validate session → call service → handle errors → log response
   - Uses @PostMapping (27), @PutMapping (4), and @DeleteMapping (4) annotations
   - Location: `src/main/java/az/kerimov/fin/finance/finamance/FinController.java`

2. **Service Layer** (`FinService.java`)
   - Single service class marked `@Transactional` for ACID compliance
   - Contains all business logic (612 lines)
   - Handles currency conversion, exchange rate lookups, transaction calculations
   - Location: `src/main/java/az/kerimov/fin/finance/finamance/FinService.java`

3. **Repository Layer**
   - 14 Spring Data JPA repositories for database access
   - Standard repository naming: `[Entity]Repository.java`
   - Location: `src/main/java/az/kerimov/fin/finance/finamance/`

### Domain Model
Key entity relationships:
- **User** → has many **UserCurrency** (active currencies for user) → references **Currency**
- **User** → has many **Wallet** (one per currency) → contains balance for that currency
- **User** → has many **Transaction** → references **Wallet**, **SubCategory**, **Orientation** (income/expense)
- **Category** → has many **SubCategory** (nested transaction categories)
- **Currency** → has many **Rate** (exchange rates by date, format: yyyyMMdd)

Database views (read-only entities):
- **TransactionReport** (`f_fn_all_transaction`) - Flattened view for transaction querying and reporting
- **SubCategoryV** (`v_fn_sub_categories`) - View with transaction counts per subcategory (uses @Expose for Gson serialization)

### Multi-Currency & Exchange Rates
- Users can add multiple currencies via **UserCurrency** (marked active/inactive)
- One currency per user is marked as default
- **Rate** entity stores historical exchange rates with `ctime` field (format: yyyyMMdd)
- Service layer includes methods to find latest rate for a given date
- Transactions can span multiple currencies with automatic conversion

### Date Handling
Three date formats are used throughout:
- `yyyyMMdd` - For exchange rate dates (ctime field)
- `yyyy-MM-dd HH:mm:ss` - For transaction timestamps
- Standard Java Date objects for calculations

Defined in `FinService.java:51-55`

### Exception Handling
Custom exceptions in `az.kerimov.fin.finance.exception/`:
- `UserNotFoundException` - Invalid user/session
- `AddCurrencyException` - Currency addition failures
- `RequestException` - Invalid request parameters
- `UserExistsException` - Duplicate user creation

All exceptions are caught by controllers and converted to Error responses with language-specific messages via the **CustomError** entity:
- CustomError entity provides localized error messages based on exception type and language
- CustomErrorRepository methods: `findByExceptionAndLang()` and `findByExceptionAndParamsAndLang()`
- Error messages support parameterization for dynamic content

## Database Configuration
- No application.properties or application.yml files in the project
- Database connection likely configured via environment variables, system properties, or external configuration
- Uses MySQL Connector 6.0.6
- JPA auto-configuration handles schema generation
- No migration scripts currently (Hibernate schema auto-update)

## Technology Notes
- Spring Boot version 1.5.7 is quite old (2017) - consider eventual upgrade
- Uses Jetty instead of Tomcat (Tomcat dependency excluded in pom.xml)
- Gson for JSON (not Jackson) - see `FinService.java` for usage patterns
- Hystrix Dashboard available for monitoring circuit breakers
- Spring Actuator enabled for application metrics

## API Endpoint Groups

All 35 endpoints use @HystrixCommand for fault tolerance. HTTP methods: 27 POST, 4 PUT, 4 DELETE.

- **Auth** (POST): /login, /logoff, /changePassword
- **User** (POST): /getUserBySession
- **Currency** (POST): /getAllCurrencies, /getUserCurrencies, /getCurrencyByCode, /setDefaultCurrency, /getActualRates, /getRateForDate
- **Currency** (PUT): /addCurrency, /addRate
- **Currency** (DELETE): /deleteCurrency
- **Wallet** (POST): /getUserWallets, /getUserInactiveWallets, /getWalletById, /activateWallet, /setDefaultWallet, /changeWalletBalance
- **Wallet** (PUT): /addWallet
- **Wallet** (DELETE): /deleteWallet
- **Category** (POST): /getCategories, /getCategoryById
- **Category** (PUT): /addCategory
- **Category** (DELETE): /deleteCategory
- **SubCategory** (POST): /getSubCategories, /getSubCategoryById
- **SubCategory** (PUT): /addSubCategory
- **SubCategory** (DELETE): /deleteSubCategory
- **Transaction** (PUT): /addTransaction
- **Transaction** (POST): /getLastTransactions (retrieves last 10), /getTransactionsBetweenDates
- **Report** (POST): /getReportList, /getOrientations
