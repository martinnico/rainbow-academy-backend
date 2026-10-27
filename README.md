# Commerce API

A RESTful e-commerce backend built with **Java** and **Spring Boot**, featuring product catalog management, purchase tracking, user management, and JWT-based authentication.

## Features

- **Product management** — full CRUD with filtering by category and color
- **Purchase management** — create, update, and query purchases by user
- **User management** — registration, login, and JWT token refresh
- **JWT Authentication** — stateless auth with access and refresh tokens via Spring Security
- **Custom exception handling** — domain-specific exceptions per entity

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8 |
| Utilities | Lombok |
| Build tool | Maven |

## Project Structure

```
src/main/java/com/olivo/commerce/
├── config/         # Security config and JWT filter
├── controller/     # REST controllers (Producto, Compra, Usuario, Auth)
├── dto/            # Request and response DTOs
├── entity/         # JPA entities (Producto, Compra, Usuario, CompraProducto)
├── exception/      # Custom domain exceptions
├── repository/     # Spring Data JPA repositories
└── service/        # Business logic layer with interfaces
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and receive JWT |
| POST | `/auth/refresh` | Refresh access token |

### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/productos/traerProductos` | Get all products |
| GET | `/api/productos/traerProducto/{id}` | Get product by ID |
| GET | `/api/productos/encontrarPorCategoria/{categoria}` | Filter by category |
| GET | `/api/productos/encontrarPorColor/{color}` | Filter by color |
| POST | `/api/productos/agregarProducto` | Create product |
| PUT | `/api/productos/modificarProducto/{id}` | Update product |
| DELETE | `/api/productos/borrarProducto/{id}` | Delete product |

### Purchases
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/compras/traerTodos` | Get all purchases |
| GET | `/api/compras/traerCompra/{id}` | Get purchase by ID |
| GET | `/api/compras/traerCompraUsuario/{nombre}` | Get purchases by user |
| POST | `/api/compras/agregarCompra` | Create purchase |
| PUT | `/api/compras/modificarCompra/{id}` | Update purchase |
| DELETE | `/api/compras/eliminarCompra/{id}` | Delete purchase |

## Getting Started

### Prerequisites

- Java 17+
- MySQL 8
- Maven

### Setup

1. Clone the repository
2. Create a MySQL database named `olivo`
3. Set the following environment variable:
```bash
export JWT_SECRET=your_secret_key_here
```
4. Update `src/main/resources/application.properties` with your DB credentials if needed
5. Run the application:
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`

## Status

🚧 In progress — core CRUD and authentication implemented. Pending: input validation, pagination, and role-based access control.# rainbow-academy-backend
# rainbow-academy-backend
