Rainbow Academy - Backend API 🌈📚

Rainbow Academy es el backend (API RESTful) para una plataforma de e-learning asincrónica especializada en la enseñanza del idioma inglés. Diseñado con una arquitectura limpia y modular, el sistema gestiona cursos por niveles, desbloqueo secuencial de lecciones mediante actividades interactivas (sopa de letras y quizzes), compras de ebooks y un sistema de seguridad granular basado en roles.

🛠️ Stack Tecnológico

Lenguaje: Java (Jakarta EE)

Framework Principal: Spring Boot 3.x

Seguridad: Spring Security + JSON Web Tokens (JWT) para autenticación Stateless.

Persistencia: Spring Data JPA / Hibernate

Base de Datos: MySQL

Herramientas: Maven, Lombok (para reducción de boilerplate)

🏗️ Arquitectura y Capas

El proyecto sigue estrictamente una Arquitectura en Capas (Layered Architecture) y cumple con los principios de diseño REST y la Tercera Forma Normal (3FN) en su base de datos. NUNCA se exponen entidades en los controladores; toda la comunicación cliente-servidor se realiza a través de DTOs (Data Transfer Objects).

controller: Endpoints REST expuestos al frontend (Next.js).

service: Lógica de negocio (ej. Motor de desbloqueo, cálculos de puntaje).

repository: Interfaces JPA para el acceso a datos.

entity: Modelos de la base de datos.

dto: Records de Java para peticiones y respuestas seguras.

security: Filtros JWT, configuración CORS y configuración RBAC.

exception: Manejo global de errores (RestControllerAdvice).

🧩 Módulos Principales

Autenticación y Seguridad (RBAC): Sistema basado en JWT. Define dos roles principales: TEACHER (acceso al panel de administración para crear contenido) y STUDENT (consumo de clases y juegos).

Gestión de Cursos y Lecciones: Organización de contenido en niveles (Principiante, Intermedio, Avanzado).

Actividades Interactivas: Motor flexible para vincular una lección con un juego específico (WORD_SEARCH o QUIZ). La lógica de la cuadrícula gráfica de la sopa de letras se delega al frontend; el backend gestiona las palabras clave y la validación.

Motor de Progreso Secuencial: Sistema que evalúa el puntaje del alumno tras realizar una actividad. Si el score es mayor o igual al minPassScore, marca la lección actual como COMPLETED y desbloquea automáticamente la siguiente (UNLOCKED).

Ebooks Digitales: Módulo de tienda que permite la compra y validación de acceso a material en PDF.

🚀 Instalación y Ejecución Local

1. Requisitos Previos

Java 17 o superior.

Maven instalado.

MySQL Server corriendo localmente (ej. XAMPP, WampServer o Docker) en el puerto 3306.

2. Configuración de la Base de Datos

Crear una base de datos vacía en MySQL llamada rainbow_academy_db. Opcionalmente, Spring Boot la creará si se mantiene el parámetro en la URL de conexión.

Configurar las credenciales en src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/rainbow_academy_db?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_contraseña_aqui

# Hibernate auto-creará las tablas
spring.jpa.hibernate.ddl-auto=update


3. Compilación y Ejecución

Ejecutar el siguiente comando en la raíz del proyecto para limpiar builds antiguos y levantar el servidor:

mvn clean spring-boot:run


La API estará disponible en http://localhost:8080.

🔐 Credenciales por Defecto (DataInitializer)

El proyecto incluye un script que puebla automáticamente la base de datos al arrancar por primera vez, creando los roles y usuarios de prueba:

Rol Profesora (Admin):

Email: profesora@rainbow.com

Password: Admin1234!

Rol Alumno (Estudiante):

Email: alumno@rainbow.com

Password: Alumno1234!