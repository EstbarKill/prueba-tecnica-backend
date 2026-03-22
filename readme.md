# 🚀 Franchise Management API

API REST desarrollada en **Spring Boot + WebFlux** para la gestión de franquicias, sucursales y productos.

---

## 🧠 Descripción

Este proyecto permite administrar:

* Franquicias
* Sucursales
* Productos
* Stock de productos

Incluye además lógica de negocio para identificar el producto con mayor stock por sucursal en una franquicia.

---

## ⚙️ Tecnologías utilizadas

* Java 17+
* Spring Boot
* Spring WebFlux (programación reactiva)
* Project Reactor (Mono / Flux)
* MongoDB
* Lombok

---

## 🏗️ Arquitectura

Se implementa **Clean Architecture**, separando:

```
application/usecase       → lógica de negocio
domain/model             → entidades
domain/repository        → contratos
infrastructure/adapters  → implementación repositorios
infrastructure/entrypoints → controladores REST
```

---

## 📡 Endpoints principales

### 🔹 Franquicias

| Método | Endpoint              | Descripción       |
| ------ | --------------------- | ----------------- |
| POST   | /franchises           | Crear franquicia  |
| PUT    | /franchises/{id}/name | Actualizar nombre |

---

### 🔹 Sucursales

| Método | Endpoint            | Descripción                       |
| ------ | ------------------- | --------------------------------- |
| POST   | /branches           | Crear sucursal                    |
| PUT    | /branches/{id}/name | Actualizar nombre                 |
| DELETE | /branches/{id}      | Eliminar sucursal y sus productos |

---

### 🔹 Productos

| Método | Endpoint             | Descripción       |
| ------ | -------------------- | ----------------- |
| POST   | /products            | Crear producto    |
| PUT    | /products/{id}/name  | Actualizar nombre |
| PUT    | /products/{id}/stock | Actualizar stock  |
| DELETE | /products/{id}       | Eliminar producto |

---

### 🔥 Consulta avanzada

| Método | Endpoint                      | Descripción                           |
| ------ | ----------------------------- | ------------------------------------- |
| GET    | /franchises/{id}/top-products | Producto con mayor stock por sucursal |

---

## 🔄 Ejemplo flujo

1. Crear franquicia
2. Crear sucursales
3. Agregar productos
4. Actualizar stock
5. Consultar producto top por sucursal

---

## ▶️ Cómo ejecutar el proyecto

### 1. Clonar repositorio

```
git clone <url-repo>
cd proyecto
```

---

### 2. Configurar MongoDB

Asegúrate de tener Mongo corriendo:

```
mongodb://localhost:27017
```

---

### 3. Ejecutar aplicación

```
./mvnw spring-boot:run
```

o

```
mvn spring-boot:run
```

---

## 🧪 Pruebas

Puedes probar los endpoints usando:

* Postman
* Insomnia

---

## 🌟 Características destacadas

* Programación reactiva (WebFlux)
* Arquitectura limpia (Clean Architecture)
* Manejo de relaciones entre entidades
* Lógica de negocio desacoplada
* Escalable y mantenible

---

## 🚀 Mejoras futuras

* Dockerización del proyecto
* Documentación con Swagger (OpenAPI)
* Manejo global de errores
* Deploy en la nube
* Infraestructura como código (Terraform)

---

## 👨‍💻 Autor
Ingeniero Estevan Cabarcas 
Desarrollado como prueba técnica Backend.
