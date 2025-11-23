package com.techlab.crud.service.UserService;

import com.techlab.crud.model.Usuario.Usuario;
import com.techlab.crud.model.Roles.Roles;
import com.techlab.crud.repository.Usuario.UserRepository;
import com.techlab.crud.repository.Roles.RoleRepository;

import com.techlab.crud.exception.RoleNoEncontradoException;
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
    private RoleRepository roleRepository; // Repositorio para buscar el rol
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inyector de BCrypt

    // --- LÓGICA DE NEGOCIO CRUCIAL: HASHING Y ASIGNACIÓN DE ROL ---
    @Override
    @Transactional
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

        // 2. Cifrado de Contraseña (Hashing)
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(hashedPassword); 
        } else if (usuario.getId() == null) {
            throw new IllegalArgumentException("Se requiere una nueva contraseña para usuario.");
        }
        if (usuario.getId() == null) {
            usuario.setActivo(true);
        }

       
        return userRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        Objects.requireNonNull(email, "El email no puede ser nulo.");
        return userRepository.findByEmailAndActivoTrue(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllActivos() {
        return userRepository.findByActivoTrue();
    }

    @Override
    @Transactional
    public void deactivateById(Long id) {
        Objects.requireNonNull(id, "El ID no puede ser nulo.");
        
        Usuario usuario = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado.")); 
        
        usuario.setActivo(false); 
        userRepository.save(usuario);
    }
}