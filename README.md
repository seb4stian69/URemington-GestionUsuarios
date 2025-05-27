# Gestión de Usuarios – Backend

Este proyecto forma parte del sistema de gestión interna de **UniRemington** y está desarrollado con **Quarkus 3.22**, el framework Java ultrarrápido y liviano.  
Tiene como objetivo **gestionar usuarios, perfiles y personas**, proporcionando una API RESTful segura, extensible y de alto rendimiento, basada en principios de arquitectura limpia.

---

## 📁 Estructura del Proyecto

El backend está organizado siguiendo los principios de Clean Architecture:

```
src/main/java/com/uniremington/
│
├── application/
│   ├── dto/                  # Objetos de transferencia de datos (DTO) para la comunicación entre capas
│   └── service/              # Lógica de negocio y casos de uso (servicios con interfaces y/o implementaciones genéricas)
│
├── domain/
│   ├── model/                # Entidades de dominio puras, sin dependencia de frameworks
│   └── repository/           # Interfaces que definen los puertos para persistencia (repositorios)
│
├── infrastructure/
│   ├── repository/           # Adaptadores de persistencia: implementaciones JPA de los repositorios
│   └── rest/                 # Adaptadores de entrada: controladores REST para exponer la API
│
└── shared/
    ├── base/                 # Clases base y utilidades genéricas reutilizables (servicios, repositorios, mapeos)
    └── exceptions/           # Manejo centralizado de excepciones y errores comunes
```

---

## ✨ Características principales

- ✅ CRUD para entidades `Persona`, `Usuario` y `Perfil`
- 🔍 Uso de DTOs para encapsular datos de entrada/salida
- 🧩 Servicios y repositorios desacoplados para pruebas y escalabilidad
- 🏗️ Base para integración de seguridad con JWT
- 📐 Preparado para extensión con eventos y colas (por ejemplo, Kafka, ActiveMQ)

---

## 🚀 Ejecutar en modo desarrollo

```bash
./mvnw quarkus:dev
```

> Accede a la Dev UI en: [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/)

---

## 📦 Empaquetar la aplicación

```bash
./mvnw package
```

Esto genera el directorio `target/quarkus-app/` con el archivo ejecutable `quarkus-run.jar`:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

Para empaquetar como _über-jar_:

```bash
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

---

## 🧊 Compilación nativa

Si tienes GraalVM instalado:

```bash
./mvnw package -Dnative
```

O usando un contenedor Docker:

```bash
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Luego ejecuta el binario generado:

```bash
./target/gestion-usuarios-1.0.0-SNAPSHOT-runner
```

---

## 📚 Requisitos del entorno

- Java 17+
- Maven 3.8.x o superior
- Docker (para builds nativos opcionales)
- GraalVM (opcional)

---

## 🔧 Extensiones de Quarkus utilizadas

- `quarkus-resteasy-reactive`
- `quarkus-hibernate-orm-panache`
- `quarkus-jdbc-postgresql`
- `quarkus-smallrye-openapi`
- `quarkus-arc`
- `quarkus-hibernate-validator`

---

## 🔐 Futuras integraciones (planeadas)

- Autenticación y autorización con JWT
- Logger audit trail de acciones por usuario
- Internacionalización (i18n)
- WebSocket para notificaciones en tiempo real

---

## 📖 Recursos útiles

- [Documentación oficial de Quarkus](https://quarkus.io/)
- [Guía de arquitectura limpia – Uncle Bob](https://8thlight.com/blog/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Guía de DTOs vs Entidades](https://reflectoring.io/spring-dto-pattern/)

---

## 🏫 Créditos

Desarrollado por el equipo académico de UniRemington  
Propósito: Modernizar y digitalizar la gestión de usuarios dentro de los sistemas administrativos y académicos.

---
© UniRemington 2025 · Todos los derechos reservados
