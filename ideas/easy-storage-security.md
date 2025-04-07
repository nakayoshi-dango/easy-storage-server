# Documentación del sistema de seguridad en Easy Storage

Este documento describe las clases encargadas de la seguridad en la aplicación **Easy Storage**.

## 1. SecurityConfig

Clase de configuración de seguridad de Spring Security.

- **Habilita la seguridad en la aplicación** con `@EnableWebSecurity` y `@EnableMethodSecurity`.
- **Configura el filtro de seguridad** (`JwtAuthenticationFilter`).
- **Deshabilita CSRF** ya que la seguridad se basa en tokens.
- **Define la política de sesión** como `STATELESS` porque se usa JWT.
- **Configura las reglas de acceso**:
  - Registro y login abiertos a todos.
  - `/users/dashboard` accesible a `ADMIN` y `USER`.
  - `/users/admin/**` solo para `ADMIN`.
  - Resto de peticiones requieren autenticación.

---

## 2. JwtUtil

Clase encargada de la generación y validación de tokens JWT.

- **Genera tokens** con usuario y rol.
- **Valida tokens** para verificar su autenticidad.
- **Extrae datos del token**:
  - `getUsernameFromToken()` devuelve el nombre del usuario.
  - `getRoleFromToken()` devuelve el rol.
- **Extrae el token del encabezado HTTP** con `getTokenFromRequest()`.

---

## 3. JwtAuthenticationFilter

Filtro que se ejecuta en cada petición para validar el JWT.

- **Extrae y valida el token** de la cabecera `Authorization`.
- **Si es válido, establece la autenticación en Spring Security**.
- **Asigna el rol del usuario en Spring Security**, permitiendo control de acceso.

---

## 4. CustomUserDetailsService

Carga los detalles del usuario desde la base de datos.

- **Busca el usuario por nombre en la base de datos**.
- **Si no lo encuentra, lanza ****`UsernameNotFoundException`**.
- **Si lo encuentra, retorna un objeto ****`UserDetails`**** con su rol asignado**.

---

## 5. AuthController

Controlador para la autenticación de usuarios.

- **`/register`**: Registra un nuevo usuario en la base de datos.
- **`/login`**:
  - Verifica credenciales.
  - Si son correctas, genera y retorna un token JWT.
  - Si no, responde con error 401.

---

### Conclusión

Este sistema implementa autenticación basada en JWT para gestionar usuarios y permisos de forma segura, permitiendo accesos diferenciados entre administradores y usuarios regulares.


