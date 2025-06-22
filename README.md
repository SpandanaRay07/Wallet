# Wallet App 💸

A simple Spring Boot wallet application that allows:

- User registration & login (with HTTP Basic Auth)
- Adding funds to your wallet
- Sending money to another user
- Viewing transaction history
- Managing and buying products using wallet balance

## 🚀 Features

- RESTful API with Spring Boot
- In-memory H2 database
- Spring Security authentication
- Clean transaction logging

## 🔧 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database

## 📦 API Endpoints

### User
- `POST /api/register` – Register a user

### Wallet
- `POST /fund` – Add funds (auth required)
- `POST /pay` – Transfer to another user (auth required)
- `GET /balance` – View current balance (auth required)

### Transactions
- `GET /transactions` – View all transactions (auth required)

### Products
- `POST /products/add` – Add product (auth required)
- `GET /products` – List all products
- `POST /buy` – Purchase product (auth required)

## 🛠 Setup

```bash
git clone https://github.com/SpandanaRay07/Wallet.git
cd Wallet
mvn spring-boot:run
