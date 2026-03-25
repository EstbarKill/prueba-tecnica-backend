# Franchise Management API
## Prueba ##
---
Se requiere construir un API para manejar una lista de franquicias, Una franquicia se compone por un nombre y una lista de
sucursales y, a su vez, una sucursal está compuesta por un nombre y un listado de productos ofertados en la sucursal. Un
producto se compone de un nombre y una cantidad de Stock.   
---

API REST reactiva para la gestión de franquicias, sucursales y productos.

**Stack:** Java 17 · Spring Boot 3 · WebFlux · MongoDB Reactive · Clean Architecture

---

## Descripción

Gestión completa de la jerarquía **Franquicia → Sucursal → Producto** con validaciones de negocio en cada operación.

 # Franquicias
  Crear y renombrar la entidad raíz del modelo de negocio.
 # Sucursales
  Crear, renombrar y eliminar sucursales vinculadas a una franquicia.
 # Productos
  Crear, renombrar, gestionar stock y eliminar productos por sucursal.
---
## Modelo de datos
---
*Franquicia — entidad raíz*
class Franchise { String id; String name; }

*Sucursal — pertenece a una Franquicia*
class Branch    { String id; String name; String franchiseId; }

*Producto — pertenece a una Sucursal*
class Product   { String id; String name; Integer stock; String branchId; }
---
## Arquitectura

**Clean Architecture** 
Separación estricta de responsabilidades. El dominio no depende de ningún framework ni tecnología de persistencia.

```
com.company.prueba_tecnica/
├── domain/
│   ├── model/              # Entidades de negocio (Franchise, Branch, Product)
│   ├── repository/         # Contratos de persistencia (interfaces)
│   └── exception/          # Excepciones de dominio
├── application/
│   └── usecase/            # Lógica de negocio (un caso de uso por operación)
│       └── dto/            # Objetos de transferencia
└── infrastructure/
    ├── entrypoints/        # Controladores REST
    ├── persistence/
    │   ├── adapter/        # Implementación de repositorios
    │   ├── document/       # Documentos MongoDB
    │   └── repository/     # Spring Data Reactive repos
    └── web/
        └── handler/        # Manejo global de errores
        └── response/       # Manejo global de respuestas de la API
```

---

## Endpoints

### Franquicias

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/franchises` | Crear franquicia |
| `PUT` | `/franchises/{id}/name` | Actualizar nombre |
| `GET` | `/franchises/structure` | Estructura completa |
| `GET` | `/franchises/{franchiseId}/branches/top-products` | Producto con mayor stock por sucursal |

### Sucursales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/branches` | Crear sucursal |
| `PUT` | `/branches/{id}/name` | Actualizar nombre |
| `DELETE` | `/{productId}/branches/{branchId}` | Eliminar producto de sucursal |

### Productos

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/products` | Crear producto |
| `PUT` | `/products/{id}/name` | Actualizar nombre |
| `PUT` | `/products/{id}/stock` | Actualizar stock |
| `DELETE` | `/products/{productId}/branches/{branchId}` | Eliminar producto de sucursal |

---

## Cuerpos de ejemplo

**Crear franquicia**
```json
POST /franchises
{ "id": "001", "name": "Mi Franquicia" }
```

**Crear sucursal**
```json
POST /branches
{ "name": "Sucursal Centro", "franchiseId": "001" }
```

**Crear producto**
```json
POST /products
{ "name": "Producto A", "stock": 100, "branchId": "lk212k1" }
```

**Respuesta top-products**
```json
{
  "id": "001",
  "name": "Mi Franquicia",
  "branches": [
    {
      "branchId": "lk212k1",
      "branchName": "Sucursal Centro",
      "topProduct": { "productId": "003", "productName": "Producto A", "stock": 100 }
    }
  ]
}
```

---

## Instalación y ejecución

**Requisitos:** Java 17+, Maven (o usar el wrapper incluido)

```bash
# 1. Clonar el repositorio
git clone <url-repo>
cd prueba-tecnica

# 2. Ejecutar
./mvnw spring-boot:run

# La API queda disponible en http://localhost:8080
```

La base de datos MongoDB Atlas ya está configurada en `application.yml`. Para usar una instancia local, cambiar el URI:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/prueba-tecnica
```
## Probar los endpoints
---

# Crear franquicia
curl -X POST http://localhost:8080/franchises \
  -H "Content-Type: application/json" \
  -d '{"id":"fr-001","name":"Mi Franquicia"}'

# Top product por sucursal
curl http://localhost:8080/franchises/fr-001/branches/top-products
---

## Tests

```bash
./mvnw test
```

Cobertura con JUnit 5 + Mockito + StepVerifier para todos los casos de uso:

- `CreateFranchiseUseCaseTest` — éxito, ID duplicado, nombre duplicado
- `CreateBranchUseCaseTest` — éxito, nombre duplicado en franquicia
- `CreateProductUseCaseTest` — éxito, sucursal no encontrada, producto duplicado
- `DeleteProductsByBranchUseCaseTest` — éxito, producto no encontrado
- `GetFranchiseStructureUseCaseTest` — estructura completa
- `GetTopProductByBranchInFranchiseUseCaseTest` — producto top por sucursal
- `UpdateFranchiseNameUseCaseTest`, `UpdateBranchNameUseCaseTest`, `UpdateProductNameUseCaseTest`, `UpdateProductStockUseCaseTest`

---

## Mejoras futuras

- [ ] Dockerización (Dockerfile + docker-compose)
---
  El proyecto no incluye contenedorización con Docker debido a limitaciones del entorno de desarrollo (sistema operativo Linux LTS) y restricciones de compatibilidad con herramientas de virtualización en la configuración actual.

  Sin embargo, la arquitectura del sistema está diseñada para ser totalmente compatible con Docker, por lo que su implementación puede agregarse fácilmente en futuras mejoras sin cambios estructurales relevantes.
---
- [ ] Documentación con Swagger / OpenAPI
- [ ] Manejo global de errores completo (404, 409, 422)
- [ ] Deploy en la nube
- [ ] Infraestructura como código (Terraform)

---

**Autor:** Ing. Estevan Cabarcas — Prueba técnica Backend
