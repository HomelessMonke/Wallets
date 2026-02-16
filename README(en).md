### 🚀 Wallet REST Service Development
---

### 📝 Task Description

It is required to implement an application that provides a REST API for wallet management.

---

### 📌 Functionality

### Wallet Balance Update:

**POST** /api/v1/wallet

Request format:

{
  "walletId": "UUID",
  "operationType": "DEPOSIT" | "WITHDRAW",
  "amount": 1000
}

After receiving the request, the application must perform the balance update logic in the database.

---

### Get Wallet Balance:

**GET** /api/v1/wallets/{WALLET_UUID}

Returns the current wallet balance.

---

### 🧾 Requirements

### Technology Stack

- Java 8–17
- Spring 3
- PostgreSQL
- Liquibase

---

### Database

- Database migrations must be implemented using Liquibase

---

### Concurrency Load

- Must handle up to 1000 RPS on a single wallet
- No request should result in a 50X error

---

### Error Handling

A unified response format must be provided for:

- Non-existent wallet
- Invalid JSON
- Insufficient funds

---

### Deployment

- The application must run in a Docker container
- The database must run in a Docker container
- The entire system must be started using docker-compose
- Application and database parameters must be configurable without rebuilding containers

---

### Testing

- Endpoints must be covered with tests
