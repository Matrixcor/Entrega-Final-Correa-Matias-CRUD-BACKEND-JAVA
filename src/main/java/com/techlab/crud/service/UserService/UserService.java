package com.techlab.crud.service.UserService;

import com.techlab.crud.model.Usuario.Usuario;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Usuario save(Usuario usuario); 
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAllActivos(); 
    void deactivateById(Long id);
}
