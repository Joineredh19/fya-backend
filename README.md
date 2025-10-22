# FYA Credits - Backend (Spring Boot)

API REST para gestionar créditos (registro, consulta, búsqueda y notificaciones por correo) con autenticación JWT.

---

## Resumen
Proyecto Java 17 + Spring Boot que expone endpoints para:
- Registrar créditos
- Listar y filtrar créditos
- Consultar crédito por ID
- Registrar/Loguear usuarios y obtener JWT
- Envío de notificaciones por correo (async)

Estructura principal:
- Controladores: AuthController, CreditController
- Servicios: AuthService, CreditService, EmailService
- Seguridad: JWT + CustomUserDetailsService + SecurityConfig
- Repositorios JPA: CreditRepository, UserRepository
- DTOs: RegisterRequest, LoginRequest, AuthResponse

---

## Tecnologías
- Java 17
- Spring Boot 3.x
- Spring Security (JWT)
- Spring Data JPA (MySQL u otra BD relacional)
- Spring Mail
- Maven (wrapper incluido)

---

## Requisitos
- JDK 17+
- MySQL (o ajustar URL para otra BD)
- Credenciales SMTP válidas para envío de correos
- Maven (wrapper incluido: use el wrapper)

---

## Configuración (application.properties)
Editar `src/main/resources/application.properties`. Claves importantes (no subir credenciales reales):

- Base de datos:
  - spring.datasource.url=jdbc:mysql://localhost:3306/fya_db
  - spring.datasource.username=tu_usuario
  - spring.datasource.password=tu_password
  - spring.jpa.hibernate.ddl-auto=update

- JWT:
  - jwt.secret=clave_muy_secreta
  - jwt.expirationMs=3600000

- Mail:
  - spring.mail.host=smtp.ejemplo.com
  - spring.mail.port=587
  - spring.mail.username=correo@ejemplo.com
  - spring.mail.password=tu_password_mail
  - spring.mail.properties.mail.smtp.auth=true
  - spring.mail.properties.mail.smtp.starttls.enable=true
  - app.email.from=correo@ejemplo.com
  - app.email.to=destinatario@ejemplo.com

- Misc:
  - server.port=8080


---

## Entidades (resumen)
Basado en el código existente (resumen orientativo):

- User
  - id (Long)
  - username / email
  - password (encriptada)
  - roles (opcional)

- Credit
  - id (Long)
  - nombreCliente (String)
  - cedula (String)
  - nombreComercial (String)
  - monto (BigDecimal / Double)
  - plazo (Integer)
  - fechaRegistro (Date/Instant)
  - estado (String)

Para campos exactos revisar `src/main/java/com/fyasocialcapital/credits/entities/`.

---

## Endpoints principales (ejemplos)
- POST /api/auth/register  
  Body (JSON):
  {
    "username": "usuario",
    "password": "pass123",
    "email": "user@ejemplo.com"
  }

- POST /api/auth/login  
  Body (JSON):
  {
    "username": "usuario",
    "password": "pass123"
  }  
  Response: `{ "token": "eyJhbGciOi..." }`

- POST /api/creditos/crear (protegido — Bearer token)
  Body (JSON ejemplo):
  {
    "nombreCliente": "Juan Pérez",
    "cedula": "12345678",
    "nombreComercial": "Negocio S.A.",
    "monto": 5000,
    "plazo": 12
  }

- GET /api/creditos/todos  
  Query params soportados: `nombreCliente`, `cedula`, `nombreComercial`, `sortBy`  
  Ejemplo: `/api/creditos/todos?nombreCliente=Juan&sortBy=fechaRegistro,desc`

- GET /api/creditos/{id}

Revisa `CreditController` y `AuthController` para rutas/contratos exactos.

---

## Uso / Ejecución (Windows)
1. Compilar:
   .\mvnw.cmd clean package

2. Ejecutar JAR:
   java -jar target\<nombre-del-jar>.jar

3. Alternativa (run con wrapper):
   .\mvnw.cmd spring-boot:run

Puerto por defecto: 8080 (configurable).

---

## Autenticación (JWT)
- Al loguear, `AuthController` devuelve un token JWT.
- Incluye token en `Authorization: Bearer <token>` para endpoints protegidos.
- `SecurityConfig` y `JwtAuthenticationFilter` validan el token en cada petición.

Ejemplo curl:
1) Login:
   curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"user\",\"password\":\"pass\"}"

2) Usar token:
   curl -H "Authorization: Bearer <token>" http://localhost:8080/api/creditos/todos

---

## Envío de correos
- `EmailService` envía correos de forma asíncrona (no bloqueante).
- Configurar SMTP en `application.properties`.
- Variables `app.email.from` y `app.email.to` usadas para notificaciones; revisar implementación para plantilla y destinatarios.


---

## Depuración / Errores comunes
- Error de conexión DB: verificar URL, usuario, puerto y que MySQL esté escuchando.
- 401 / 403: revisar header Authorization y validez del JWT (fecha/clave).
- Problemas de envío de correo: revisar credenciales SMTP y puerto (TLS/SSL).

---

## Dónde mirar en el proyecto
- src/main/java/com/fyasocialcapital/credits/controller — rutas y contratos
- src/main/java/com/fyasocialcapital/credits/services — lógica de negocio
- src/main/java/com/fyasocialcapital/credits/security — JWT y configuración de seguridad
- src/main/resources/application.properties — configuración principal

---

