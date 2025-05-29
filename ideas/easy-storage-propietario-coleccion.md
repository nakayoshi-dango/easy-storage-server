✅ **Ventaja:** Puedes hacer validaciones más avanzadas, como verificar si el usuario es el dueño de un recurso.
¿dueño? me interesa para que el request de editar colección sólo sea accesible al usuario dueño y a los administradores.
ChatGPT said:

Para asegurarte de que solo el usuario dueño o un administrador pueda editar su colección, puedes hacer lo siguiente con @PreAuthorize y @PostAuthorize:
✅ 1. Usar @PreAuthorize con el ID del usuario autenticado

Puedes agregar una verificación antes de ejecutar el método en el controlador.
🔹 Ejemplo en CollectionController

@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
@PutMapping("/{userId}/edit")
public ResponseEntity<String> editCollection(@PathVariable Long userId) {
    return ResponseEntity.ok("Colección editada correctamente.");
}

🔍 Explicación:

    Si el usuario es ADMIN, puede editar cualquier colección.

    Si el usuario tiene el mismo userId que el autenticado, puede editar su propia colección.

    authentication.principal.id se obtiene del usuario autenticado en el contexto de seguridad.

✅ 2. Usar @PostAuthorize si necesitas verificar después de obtener la colección

Si primero necesitas buscar la colección en la base de datos antes de decidir si tiene permiso o no, usa @PostAuthorize:

@PostAuthorize("hasRole('ADMIN') or returnObject.ownerId == authentication.principal.id")
@GetMapping("/{collectionId}")
public Collection getCollection(@PathVariable Long collectionId) {
    return collectionService.findById(collectionId);
}

🔍 Explicación:

    Primero obtiene la colección.

    Luego verifica si el usuario autenticado es el dueño (ownerId) o es ADMIN.

    Si no cumple, Spring bloquea el acceso automáticamente.

🛠 Paso extra: Configurar authentication.principal.id correctamente

Para que authentication.principal.id funcione, necesitas sobreescribir UserDetails y devolver el ID del usuario autenticado:

public class CustomUserDetails implements UserDetails {
    private Long id; // Guarda el ID del usuario
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = user.getAuthorities();
    }

    public Long getId() { // Método necesario para @PreAuthorize
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}

Esto hace que authentication.principal.id esté disponible para @PreAuthorize.
🚀 Conclusión

Si solo quieres controlar acceso antes de procesar la petición:
✅ Usa @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")

Si primero necesitas obtener la colección y luego decidir:
✅ Usa @PostAuthorize("hasRole('ADMIN') or returnObject.ownerId == authentication.principal.id")

Así garantizas que solo los dueños o administradores puedan editar una colección. 🔒
