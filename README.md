# customer-service

Microservicio responsable de la gestión de clientes bancarios personales y empresariales.

## Tecnologías

* Java 17
* Spring Boot
* Spring WebFlux
* RxJava
* Spring Data Reactive MongoDB
* MongoDB
* Spring Cloud Config Client
* Maven
* Lombok
* OpenAPI
* Logback

## Puerto

```text
8081
```

## Configuración externa

Este microservicio obtiene su configuración desde `config-server`.

Archivo local mínimo:

```yaml
spring:
  application:
    name: customer-service

  config:
    import: optional:configserver:http://localhost:8888
```

Configuración en Config Server:

```yaml
server:
  port: 8081

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/customer_db

logging:
  level:
    root: INFO
    com.bank.customer: DEBUG
```

## Levantar servicios requeridos

Desde el proyecto donde está el `docker-compose.yml`:

```bash
docker compose up -d
```

Verificar MongoDB:

```bash
docker ps
```

Levantar primero:

```text
1. MongoDB
2. config-server
3. customer-service
```

## Ejecutar customer-service

```bash
mvn clean spring-boot:run
```

## Verificar Config Server

```http
GET http://localhost:8888/customer-service/default
```

## Endpoints principales

```http
POST   http://localhost:8081/api/v1/customers
GET    http://localhost:8081/api/v1/customers
GET    http://localhost:8081/api/v1/customers/{id}
PUT    http://localhost:8081/api/v1/customers/{id}
DELETE http://localhost:8081/api/v1/customers/{id}
```

## Ejemplo POST

```json
{
  "documentNumber": "12345678",
  "fullName": "Roberto Herrera",
  "customerType": "PERSONAL",
  "email": "roberto@email.com",
  "phone": "999999999",
  "address": "Lima, Peru"
}
```

## OpenAPI

Contrato ubicado en:

```text
src/main/resources/openapi/customer-api.yml
```

Generar código:

```bash
mvn clean generate-sources
```

## Logs

Los logs se visualizan en consola al ejecutar el servicio.

Ejemplo:

```text
INFO  Creating customer with document number: 12345678
INFO  Customer created successfully with id: ...
WARN  Customer already exists with document number: 12345678
ERROR Error creating customer: ...
```

