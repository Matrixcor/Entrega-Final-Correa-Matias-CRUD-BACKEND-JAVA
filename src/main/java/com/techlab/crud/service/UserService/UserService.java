package com.techlab.crud.service.UserService;

import com.techlab.crud.model.Usuario.Usuario;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // Método para crear o actualizar un usuario (incluye hashing de contraseña)
    Usuario save(Usuario usuario); 
    
    // Método para buscar un usuario por su ID
    Optional<Usuario> findById(Long id);
    
    // Método para buscar por email (esencial para login)
    Optional<Usuario> findByEmail(String email);
    
    // Método para listar todos los usuarios activos
    List<Usuario> findAllActivos(); 
    
    // Método para borrar lógicamente un usuario (cambiar activo a false)
    void deactivateById(Long id);
}
