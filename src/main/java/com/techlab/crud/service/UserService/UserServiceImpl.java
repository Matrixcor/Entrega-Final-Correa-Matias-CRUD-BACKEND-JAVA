package com.techlab.crud.service.UserService;

import com.techlab.crud.model.Usuario.Usuario;
import com.techlab.crud.model.Roles.Roles;
import com.techlab.crud.repository.Usuario.UserRepository;
import com.techlab.crud.repository.Roles.RoleRepository;

import com.techlab.crud.exception.RoleNoEncontradoException;
import com.techlab.crud.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Necesario para hashing
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // HASHING Y ASIGNACIÓN DE ROL
    @Override
    //@Transactional
    @Transactional("usuarioTransactionManager")
    public Usuario save(Usuario usuario) {
        Objects.requireNonNull(usuario, "El objeto usuario no puede ser nulo.");
    
        if (usuario.getRole() == null || usuario.getRole().getId() == null) {
            throw new IllegalArgumentException("Debe asignarse un ID de rol válido al usuario.");
        }
        
        Long roleId = usuario.getRole().getId();
        Objects.requireNonNull(roleId, "El ID del usuario no puede ser nulo");
        Optional<Roles> roleOpt = roleRepository.findById(roleId);
        
        if (roleOpt.isEmpty()) {
            throw new RoleNoEncontradoException(roleId);
        }
        
        usuario.setRole(roleOpt.get());

        // --- 2. Gestión de Contraseña y Estado Activo ---
        if (usuario.getId() != null) {

            Usuario usuarioExistente = userRepository.findById(Objects.requireNonNull(usuario.getId()))
            .orElseThrow(() -> new UsuarioNoEncontradoException(usuario.getId()));

            // Si el payload NO trae una nueva contraseña (es nulo o vacío), PRESERVAMOS la antigua.
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            }
        
            usuario.setActivo(usuarioExistente.getActivo()); 

        } else {

        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Se requiere una contraseña para el registro de un nuevo usuario.");
        }

        usuario.setActivo(true);
    }
    
    // HASHING si hay una nueva contraseña
    if (usuario.getPassword() != null && !usuario.getPassword().isEmpty() && !usuario.getPassword().startsWith("$2a$")) {
        String hashedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(hashedPassword);
    }
    
    return userRepository.save(usuario);
    }

    @Override
    @Transactional("usuarioTransactionManager")
    public Optional<Usuario> findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional("usuarioTransactionManager")
    public Optional<Usuario> findByEmail(String email) {
        Objects.requireNonNull(email, "El email no puede ser nulo.");
        return userRepository.findByEmailAndActivoTrue(email);
    }

    @Override
   @Transactional("usuarioTransactionManager")
    public List<Usuario> findAllActivos() {
        return userRepository.findByActivoTrue();
    }

    @Override
    @Transactional("usuarioTransactionManager")
    public void deactivateById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        
        Usuario usuario = userRepository.findById(id) 
            .orElseThrow(() -> new UsuarioNoEncontradoException(id));        
        usuario.setActivo(false); 
        userRepository.save(usuario);
    }
}