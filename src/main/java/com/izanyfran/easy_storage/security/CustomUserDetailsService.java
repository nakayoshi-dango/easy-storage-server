package com.izanyfran.easy_storage.security;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.repository.RepositoryUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final RepositoryUser userRepository;

    public CustomUserDetailsService(RepositoryUser userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cargar el usuario de la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Crear un SimpleGrantedAuthority para el rol del usuario
        // (asumimos que el campo role tiene el valor como 'ROLE_USER' o 'ROLE_ADMIN')
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))  // Asignamos el rol
        );
    }
}
